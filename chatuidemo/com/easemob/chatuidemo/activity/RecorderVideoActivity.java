package com.easemob.chatuidemo.activity;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.dcy.psychology.R;
import com.easemob.chatuidemo.video.util.Utils;
import com.easemob.util.EMLog;
import com.easemob.util.PathUtil;

public class RecorderVideoActivity extends BaseActivity implements
		OnClickListener, Callback, OnErrorListener, OnInfoListener {

	private final static String CLASS_LABEL="RecordActivity";
	private PowerManager.WakeLock mWakeLock;
	private ImageView btnStart;// 寮�濮嬪綍鍒舵寜閽�
	private ImageView btnStop;// 鍋滄褰曞埗鎸夐挳
	private MediaRecorder mediarecorder;// 褰曞埗瑙嗛鐨勭被
	private SurfaceView surfaceview;// 鏄剧ず瑙嗛鐨勬帶浠�

	private SurfaceHolder surfaceHolder;
	String localPath = "";// 褰曞埗鐨勮棰戣矾寰�
	private Camera mCamera;
	//棰勮鐨勫楂�
	private int previewWidth=480;
	private int previewHeight=480;
	
	Parameters cameraParameters=null;
	

	//鍒嗗埆涓� 榛樿鎽勫儚澶达紙鍚庣疆锛夈�侀粯璁よ皟鐢ㄦ憚鍍忓ご鐨勫垎杈ㄧ巼銆佽閫夋嫨鐨勬憚鍍忓ご锛堝墠缃垨鑰呭悗缃級
	int defaultCameraId = -1, defaultScreenResolution = -1 , cameraSelection = 0;
	int defaultVideoFrameRate=-1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 鍘绘帀鏍囬鏍�
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 璁剧疆鍏ㄥ睆
		// 閫夋嫨鏀寔鍗婇�忔槑妯″紡锛屽湪鏈塻urfaceview鐨刟ctivity涓娇鐢�
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(R.layout.recorder_activity);
		PowerManager pm=(PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock=pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, CLASS_LABEL);
		mWakeLock.acquire();
		
		
		
		btnStart = (ImageView) findViewById(R.id.recorder_start);
		btnStop = (ImageView) findViewById(R.id.recorder_stop);
		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		surfaceview = (SurfaceView) this.findViewById(R.id.surfaceview);
		SurfaceHolder holder = surfaceview.getHolder();// 鍙栧緱holder
		holder.addCallback(this); // holder鍔犲叆鍥炶皟鎺ュ彛
		// setType蹇呴』璁剧疆锛岃涓嶅嚭閿�.
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void back(View view) {

		if (mediarecorder != null) {
			// 鍋滄褰曞埗
			mediarecorder.stop();
			// 閲婃斁璧勬簮
			mediarecorder.release();
			mediarecorder = null;
		}
		try {
			mCamera.reconnect();
		} catch (IOException e) {
			Toast.makeText(this, "reconect fail", 0).show();
		}
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mWakeLock == null) {
			// 鑾峰彇鍞ら啋閿�,淇濇寔灞忓箷甯镐寒
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
					CLASS_LABEL);
			mWakeLock.acquire();
		}
	}
	
	
	private void handleSurfaceChanged(){
		if(mCamera==null)
		{
			finish();
			return;
		}
		
		boolean hasSupportRate=false;
		List<Integer> supportedPreviewFrameRates = mCamera.getParameters().getSupportedPreviewFrameRates();
		if(supportedPreviewFrameRates!=null&&supportedPreviewFrameRates.size()>0)
		{
			Collections.sort(supportedPreviewFrameRates);
			for(int i=0;i<supportedPreviewFrameRates.size();i++)
			{
				int supportRate=supportedPreviewFrameRates.get(i);
				
				if(supportRate==10)
				{
					hasSupportRate=true;
				}
				
			}
			if(hasSupportRate)
			{
				defaultVideoFrameRate=10;
			}else{
				defaultVideoFrameRate=supportedPreviewFrameRates.get(0);
			}
			
			
			
		}
		
		System.out.println("supportedPreviewFrameRates"+supportedPreviewFrameRates);
		
		
		//鑾峰彇鎽勫儚澶寸殑鎵�鏈夋敮鎸佺殑鍒嗚鲸鐜�
		List<Camera.Size> resolutionList=Utils.getResolutionList(mCamera);
		if(resolutionList!=null&&resolutionList.size()>0)
		{
			Collections.sort(resolutionList,new Utils.ResolutionComparator());
			Camera.Size previewSize=null;
			if(defaultScreenResolution==-1)
			{
				boolean hasSize=false;
				//濡傛灉鎽勫儚澶存敮鎸�640*480锛岄偅涔堝己鍒惰涓�640*480
				for(int i=0;i<resolutionList.size();i++)
				{
					Size size=resolutionList.get(i);
					if(size!=null&&size.width==640&&size.height==480)
					{
						previewSize=size;
						previewWidth=previewSize.width;
						previewHeight=previewSize.height;
						hasSize=true;
						break;
					}
				}
				//濡傛灉涓嶆敮鎸佽涓轰腑闂寸殑閭ｄ釜
				if(!hasSize)
				{
					int mediumResolution=resolutionList.size()/2;
					if(mediumResolution>=resolutionList.size())
						mediumResolution=resolutionList.size()-1;
					previewSize=resolutionList.get(mediumResolution);
					previewWidth=previewSize.width;
					previewHeight=previewSize.height;
						
				}
				 
			} 
			 
		}
		
		
		
		
		
	}
	
	 
	@Override
	protected void onPause() {
		super.onPause();
		if (mWakeLock != null) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}
	 
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.recorder_start:
			mCamera.unlock();
			mediarecorder = new MediaRecorder();// 鍒涘缓mediarecorder瀵硅薄
			mediarecorder.reset();
			mediarecorder.setCamera(mCamera);
			mediarecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
			// 璁剧疆褰曞埗瑙嗛婧愪负Camera锛堢浉鏈猴級
			mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			// 璁剧疆褰曞埗瀹屾垚鍚庤棰戠殑灏佽鏍煎紡THREE_GPP涓�3gp.MPEG_4涓簃p4
			mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
			// 璁剧疆褰曞埗鐨勮棰戠紪鐮乭263 h264
			mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
			// 璁剧疆瑙嗛褰曞埗鐨勫垎杈ㄧ巼銆傚繀椤绘斁鍦ㄨ缃紪鐮佸拰鏍煎紡鐨勫悗闈紝鍚﹀垯鎶ラ敊
			mediarecorder.setVideoSize(previewWidth, previewHeight);
//			// 璁剧疆褰曞埗鐨勮棰戝抚鐜囥�傚繀椤绘斁鍦ㄨ缃紪鐮佸拰鏍煎紡鐨勫悗闈紝鍚﹀垯鎶ラ敊
			if (defaultVideoFrameRate != -1) {
				mediarecorder.setVideoFrameRate(defaultVideoFrameRate);
			}
			mediarecorder.setPreviewDisplay(surfaceHolder.getSurface());
			// 璁剧疆瑙嗛鏂囦欢杈撳嚭鐨勮矾寰�
			localPath = PathUtil.getInstance().getVideoPath() + "/"
					+ System.currentTimeMillis() + ".mp4";
			mediarecorder.setOutputFile(localPath);
			mediarecorder.setOnErrorListener(this);
			mediarecorder.setOnInfoListener(this);
			try {
				// 鍑嗗褰曞埗
				mediarecorder.prepare();
				// 寮�濮嬪綍鍒�
				mediarecorder.start();
				Toast.makeText(this, "褰曞儚寮�濮�", Toast.LENGTH_SHORT).show();
				btnStart.setVisibility(View.INVISIBLE);
				btnStop.setVisibility(View.VISIBLE);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			break;
		case R.id.recorder_stop:

			if (mediarecorder != null) {
				// 鍋滄褰曞埗
				mediarecorder.stop();
				// 閲婃斁璧勬簮
				mediarecorder.release();
				mediarecorder = null;
			}
			try {
				mCamera.reconnect();
			} catch (IOException e) {
				Toast.makeText(this, "reconect fail", 0).show();
			}
			btnStart.setVisibility(View.VISIBLE);
			btnStop.setVisibility(View.INVISIBLE);

			new AlertDialog.Builder(this)
					.setMessage("鏄惁鍙戦�侊紵")
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									arg0.dismiss();
									sendVideo(null);

								}
							}).setNegativeButton(R.string.cancel, null).show();

			break;

		default:
			break;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// 灏唄older锛岃繖涓猦older涓哄紑濮嬪湪oncreat閲岄潰鍙栧緱鐨刪older锛屽皢瀹冭祴缁檚urfaceHolder
		surfaceHolder = holder;

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// 灏唄older锛岃繖涓猦older涓哄紑濮嬪湪oncreat閲岄潰鍙栧緱鐨刪older锛屽皢瀹冭祴缁檚urfaceHolder
		surfaceHolder = holder;
		initpreview();
		handleSurfaceChanged();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// surfaceDestroyed鐨勬椂鍊欏悓鏃跺璞¤缃负null
		surfaceview = null;
		surfaceHolder = null;
		mediarecorder = null;
		releaseCamera();
	}

	protected void releaseCamera() {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	@SuppressLint("NewApi")
	protected void initpreview() {
		try {
			
			if(Build.VERSION.SDK_INT>Build.VERSION_CODES.FROYO)
			{
				int numberOfCameras=Camera.getNumberOfCameras();
				CameraInfo cameraInfo=new CameraInfo();
				for (int i = 0; i < numberOfCameras; i++) {
					Camera.getCameraInfo(i, cameraInfo);
					if(cameraInfo.facing==cameraSelection)
					{
						defaultCameraId=i;
					}
				}
				
				
			}
			if(mCamera!=null)
			{
				mCamera.stopPreview();
			}

			mCamera = Camera.open(CameraInfo.CAMERA_FACING_BACK);
			mCamera.setPreviewDisplay(surfaceHolder);
			setCameraDisplayOrientation(this, CameraInfo.CAMERA_FACING_BACK,
					mCamera);
			mCamera.startPreview();
		} catch (Exception e) {
			EMLog.e("###", e.getMessage());
			showFailDialog();
			return;
		}

	}

	@SuppressLint("NewApi")
	public static void setCameraDisplayOrientation(Activity activity,
			int cameraId, android.hardware.Camera camera) {
		android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		camera.setDisplayOrientation(result);
	}

	MediaScannerConnection msc = null;

	public void sendVideo(View view) {
		if (TextUtils.isEmpty(localPath)) {
			EMLog.e("Recorder", "recorder fail please try again!");
			return;
		}

		msc = new MediaScannerConnection(this,
				new MediaScannerConnectionClient() {

					@Override
					public void onScanCompleted(String path, Uri uri) {
						System.out.println("scanner completed");
						msc.disconnect();
						setResult(RESULT_OK, getIntent().putExtra("uri", uri));
						finish();
					}

					@Override
					public void onMediaScannerConnected() {
						msc.scanFile(localPath, "video/*");
					}
				});
		msc.connect();

	}

	@Override
	public void onInfo(MediaRecorder arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(MediaRecorder arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseCamera();
		
		if(mWakeLock!=null)
		{
			mWakeLock.release();
			mWakeLock=null;
		}
		
	}

	@Override
	public void onBackPressed() {
		back(null);
	}
	
	
	
	
	
	private void showFailDialog(){
		new AlertDialog.Builder(this).setTitle("鎻愮ず").setMessage("鎵撳紑璁惧澶辫触锛�").setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
				
			}
		}).setCancelable(false).show();
		
		
	}
	
	 
	

}
