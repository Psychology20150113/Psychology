package com.dcy.psychology;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.json.JSONObject;

import com.dcy.psychology.R;
import com.dcy.psychology.util.IOUtils;
import com.dcy.psychology.util.ShareUtils;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.CustomProgressDialog;
import com.dcy.psychology.view.crop.Crop;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.umeng.analytics.MobclickAgent;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends Activity {
	private LinearLayout rootView;
	private View mTitleView;
	private TextView mTitleText;
	protected LayoutInflater mInflater;
	private View infoLayout;
	protected Resources mResources;
	private CustomProgressDialog mDialog;
	private TextView mTopRightText;
	private ImageView mLeftView;
	private ImageView mRightView;
	private PopupWindow mShareWindow;
	protected ShareUtils mShareUtils;
	
	private PopupWindow mChoosePopup;
    private final int Request_Camera = 100;
    private final int Request_Album = 101;
    private File cameraFile;
    private UploadManager mUploadManager;
    private String qiNiuPreUrl;
	
	private OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.top_right_tv:
				onRightTextClick();
				break;
			case R.id.ll_left:
				onLeftViewClick();
				break;
			case R.id.top_right_iv:
				onRightViewClick();
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(this);
		mResources = getResources();
		rootView = new LinearLayout(this);
		rootView.setOrientation(LinearLayout.VERTICAL);
		mTitleView = mInflater.inflate(R.layout.custom_title_layout, null);
		rootView.addView(mTitleView, new LayoutParams(
				LayoutParams.MATCH_PARENT, (int)(50 * mResources.getDisplayMetrics().density)));
		initTitleView();
	}
	
	@Override
	public void setContentView(int layoutResID) {
		mInflater.inflate(layoutResID, rootView);
		super.setContentView(rootView);
	}
	
	@Override
	public void setContentView(View view) {
		rootView.addView(view);
		super.setContentView(rootView);
	}
	
	protected void showChoosePicPopupView() {
        if (mChoosePopup == null) {
            View popupLayout = getLayoutInflater().inflate(R.layout.popup_choose_pic, null);
            mChoosePopup = new PopupWindow(popupLayout, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mChoosePopup.setAnimationStyle(R.style.AnimBottom);
            mChoosePopup.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
            mChoosePopup.setOutsideTouchable(true);
            popupLayout.findViewById(R.id.take_phone_tv).setOnClickListener(choosePicListener);
            popupLayout.findViewById(R.id.choose_album_tv).setOnClickListener(choosePicListener);
            popupLayout.findViewById(R.id.cancel_tv).setOnClickListener(choosePicListener);
        }
        mChoosePopup.showAtLocation(findViewById(R.id.top_title_tv), Gravity.BOTTOM, 0, 0);
    }
	
	private View.OnClickListener choosePicListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.take_phone_tv:
                    selectPicFromCamera();
                    mChoosePopup.dismiss();
                    break;
                case R.id.choose_album_tv:
                    selectPicFromAlbum();
                    mChoosePopup.dismiss();
                    break;
                case R.id.cancel_tv:
                    mChoosePopup.dismiss();
                    break;
            }
        }
    };

    private void selectPicFromCamera() {
        File dir = IOUtils.getExternalDir();
        if (dir == null) {
            return;
        }
        cameraFile = new File(dir, System.currentTimeMillis() + ".png");
        Intent mCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
        mCameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, Configuration.ORIENTATION_PORTRAIT);
        startActivityForResult(mCameraIntent, Request_Camera);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Request_Camera:
                if (cameraFile != null && cameraFile.exists()) {
//                  addCoverView.setImageBitmap(scaleAndRotatePic(cameraFile));
                    startPhotoZoom(Uri.fromFile(cameraFile));
                }
                break;
            case Request_Album:
                if (data == null) {
                    return;
                }
                startPhotoZoom(data.getData());
                break;
            case Crop.REQUEST_CROP:
                if (data != null) {
                    if (RESULT_OK == resultCode) {
                        Bitmap mCropBitmap = BitmapFactory.decodeFile(Crop.getOutput(data).getPath());
                        onCropFinish(mCropBitmap);
                        uploadBitmap(mCropBitmap);
                    } else {
                        Toast.makeText(this, Crop.getError(data).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    protected void onCropFinish(Bitmap cropBitmap) {
    }

    protected void onUploadFinish(String url) {
    }

    private void uploadBitmap(final Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        showCustomDialog();
        new GetQiniuTokenTask().execute(bitmap);
    }
    
    private class GetQiniuTokenTask extends AsyncTask<Bitmap, Void, String>{
    	private Bitmap mBitmap;
    	
    	@Override
    	protected String doInBackground(Bitmap... params) {
    		if(params[0] == null){
    			return "";
    		}
    		mBitmap = params[0];
    		return Utils.getQiniuToken();
    	}
    	
    	@Override
    	protected void onPostExecute(String response) {
    		if(TextUtils.isEmpty(response)){
    			return;
    		}
    		String token = "";
            String fileName = "";
            try {
                JSONObject result = new JSONObject(response);
                token = result.getString("Token");
                fileName = result.getString("FileName");
                qiNiuPreUrl = result.getString("Domain");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mUploadManager == null) {
                mUploadManager = new UploadManager();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            mUploadManager.put(baos.toByteArray(), fileName, token, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo info, JSONObject response) {
                	hideCustomDialog();
                    onUploadFinish(qiNiuPreUrl + key);
                }
            }, null);
    	}
    }

    private void startPhotoZoom(Uri uri) {
        Crop mCrop = new Crop(uri).output(Uri.fromFile(IOUtils.getExternalTempFile()));
        mCrop.withAspect(1, 1).withMaxSize(250, 250);
        mCrop.start(this);
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("crop", true);
//        intent.putExtra("aspectX", 5);
//        intent.putExtra("aspectY", 3);
//        intent.putExtra("outputX", 500);
//        intent.putExtra("outputY", 300);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, Request_Crop);
    }

    private void selectPicFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, Request_Album);
    }
	
	private void initTitleView(){
		mTitleText = (TextView) mTitleView.findViewById(R.id.top_title_tv);
		mTopRightText = (TextView) mTitleView.findViewById(R.id.top_right_tv);
		mTopRightText.setOnClickListener(mClickListener);
		mLeftView = (ImageView) mTitleView.findViewById(R.id.top_left_iv);
		//mLeftView.setOnClickListener(mClickListener);
		mLeftView.setImageResource(R.drawable.icon_back);
		infoLayout = mTitleView.findViewById(R.id.ll_left);
		infoLayout.setOnClickListener(mClickListener);
		mRightView = (ImageView) mTitleView.findViewById(R.id.top_right_iv);
		mRightView.setOnClickListener(mClickListener);
	}
	
	public void setTopTitle(int resId){
		mTitleText.setText(getString(resId));
	}
	
	public void setTopTitle(String res){
		mTitleText.setText(res);
	}
	
	public void setLeftView(int resId){
		mLeftView.setImageResource(resId);
	}
	
	public void setRightText(int resId){
		mTopRightText.setText(getString(resId));
	}
	
	public void setRightText(String res){
		mTopRightText.setText(res);
	}
	
	public void hideRightText(){
		mTopRightText.setVisibility(View.GONE);
	}
	
	public void setRightView(int resId){
		mRightView.setVisibility(View.VISIBLE);
		mRightView.setImageResource(resId);
	}
	
	public void hideTitleView(){
		mTitleView.setVisibility(View.GONE);
	}
	
	public void setTitleViewColor(int color){
		mTitleView.setBackgroundColor(color);
	}
	
	public void onRightTextClick(){};
	
	public void onLeftViewClick(){finish();};
	
	public void onRightViewClick(){};
	
	protected void showCustomDialog(){
		if(mDialog == null){
			mDialog = new CustomProgressDialog(this);
		}
		mDialog.show();
	}
	
	protected void hideCustomDialog(){
		if(mDialog != null && mDialog.isShowing())
			mDialog.dismiss();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	protected void showSharePopupWindow(){
		if(mShareWindow == null){
			View shareView = mInflater.inflate(R.layout.popup_share_layout, null);
			mShareWindow = new PopupWindow(shareView, LayoutParams.MATCH_PARENT, 
					LayoutParams.WRAP_CONTENT);
			mShareWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
			mShareWindow.setAnimationStyle(R.style.AnimBottom);
			mShareWindow.setOutsideTouchable(true);
			shareView.findViewById(R.id.iv_share_ours).setOnClickListener(mShareClickListener);
			shareView.findViewById(R.id.iv_share_circle).setOnClickListener(mShareClickListener);
			shareView.findViewById(R.id.iv_share_sina).setOnClickListener(mShareClickListener);
		}
		mShareWindow.showAtLocation(mTitleView, Gravity.BOTTOM, 0, 0);
	}
	
	private OnClickListener mShareClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(mShareUtils == null){
				mShareUtils = ShareUtils.getInstance(BaseActivity.this);
			}
			switch (v.getId()) {
			case R.id.iv_share_ours:
				shareToOurs();
				break;
			case R.id.iv_share_circle:
				shareToCircle();
				break;
			case R.id.iv_share_sina:
				shareToSina();
				break;
			default:
				break;
			}
		}
	};
	
	protected void shareToOurs(){};
	
	protected void shareToCircle(){};
	
	protected void shareToSina(){};
}
