package com.dcy.psychology;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.dcy.psychology.gsonbean.ArticleBean;
import com.dcy.psychology.gsonbean.ClassBean;
import com.dcy.psychology.gsonbean.GrowPictureBean;
import com.dcy.psychology.util.AsyncImageCache;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.IOUtils;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.util.AsyncImageCache.NetworkImageGenerator;

import android.content.res.AssetManager;
import android.content.res.Resources.NotFoundException;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PlamPictureDetailActivity extends BaseActivity {
	private ImageView mPicView;
	private TextView mContentText;
	private AssetManager manager;
	private GrowPictureBean detailBean;
	private int onlineArticalId = -1;
	private int onlineClassId = -1;
	private AsyncImageCache mCache;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_platform_pic_detail_activity);
		manager = getAssets();
		mPicView = (ImageView) findViewById(R.id.pic_iv);
		mContentText = (TextView) findViewById(R.id.content_tv);
		detailBean = (GrowPictureBean) getIntent().getSerializableExtra(Constants.PictureBean);
		if(detailBean != null){
			setTopTitle(detailBean.getTitle());
			loadLocalRes();
		} else {
			onlineArticalId = getIntent().getIntExtra(Constants.OnlineArticleId, -1);
			onlineClassId = getIntent().getIntExtra(Constants.OnlineClassId, -1);
			if(onlineArticalId == -1 && onlineClassId == -1){
				return;
			}
			mCache = AsyncImageCache.from(this);
			showCustomDialog();
			if(onlineArticalId != -1){
				new LoadOnlineInfoTask().execute();
			} else if (onlineClassId != -1) {
				new LoadOnlineClassInfoTask().execute();
			}
		}
	}
	
	private void loadLocalRes(){
		try {
			InputStream stream = manager.open(detailBean.getPicture());
			mPicView.setImageBitmap(BitmapFactory.decodeStream(stream));
			mContentText.setText(IOUtils.convertStreamToString(manager.open(detailBean.getPicture().split("\\.")[0] + ".txt")));
//			mContentText.setText(Utils.loadRawString(this, getResources().getIdentifier(detailBean.getContent(), "raw", getPackageName())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mCache != null){
			mCache.stop();
		}
	}
	
	private class LoadOnlineInfoTask extends AsyncTask<Void, Void, ArticleBean>{
		@Override
		protected ArticleBean doInBackground(Void... params) {
			return Utils.getArticleInfo(onlineArticalId);
		}
		
		@Override
		protected void onPostExecute(ArticleBean result) {
			hideCustomDialog();
			if(result == null){
				return;
			}
			setTopTitle(result.getArticleName());
			mContentText.setText(result.getArticleContent());
			mCache.displayImage(mPicView, R.drawable.ic_launcher, 
					new AsyncImageCache.NetworkImageGenerator(result.getArticleImgUrl(), result.getArticleImgUrl()));
		}
	}
	
	private class LoadOnlineClassInfoTask extends AsyncTask<Void, Void, ClassBean>{
		@Override
		protected ClassBean doInBackground(Void... params) {
			return Utils.getClassInfo(onlineClassId);
		}
		
		@Override
		protected void onPostExecute(ClassBean result) {
			hideCustomDialog();
			if(result == null){
				return;
			}
			setTopTitle(result.getClassTitleName());
			mContentText.setText(result.getClassContent());
			mCache.displayImage(mPicView, R.drawable.ic_launcher, 
					new AsyncImageCache.NetworkImageGenerator(result.getClassImgUrl(), result.getClassImgUrl()));
		}
	}
}
