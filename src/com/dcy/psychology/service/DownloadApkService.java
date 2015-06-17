package com.dcy.psychology.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.dcy.psychology.R;
import com.dcy.psychology.util.NetWorkUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadApkService extends Service {
	private Notification notification;
	private NotificationManager manager;
	private LoadTask mLoadTask;
	
	private final int DOWNLOAD_NOTY_ID = 100;
	private final String DOWNLOAD_FILE_NAME = "Psychology.apk";
	private final String INSTALL_TYPE = "application/vnd.android.package-archive";
	public static final String URL_KEY = "download_url";
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent == null)
			return super.onStartCommand(intent, flags, startId);
		String url = intent.getStringExtra(URL_KEY);
		if(NetWorkUtils.getNetworkType(this) != -1){
			if(mLoadTask == null && !TextUtils.isEmpty(url)){
				mLoadTask = new LoadTask();
				mLoadTask.execute(url);
			}
		}else {
			Toast.makeText(this, R.string.network_disconnect, Toast.LENGTH_SHORT).show();
			stopSelf();
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		if(mLoadTask != null && AsyncTask.Status.RUNNING == mLoadTask.getStatus())
			mLoadTask.cancel(true);
		super.onDestroy();
	}
	
	private class LoadTask extends AsyncTask<String, Integer, String>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			initNotification();
		}
		
		@Override
		protected String doInBackground(String... params) {
			InputStream is = null;
			FileOutputStream fos = null;
			try {
				String urlString = params[0];
				URL apkUrl = new URL(urlString);
				URLConnection connection = apkUrl.openConnection();
				connection.connect();
				is = connection.getInputStream();
				int fileSize = connection.getContentLength();
				int finishSize = 0;
				if(is == null){
					throw new RuntimeException("connection stream is null");
				}
				File apkFile = getDownloadFile();
				fos = new FileOutputStream(apkFile);
				byte buf[] = new byte[1024];
				int oldPercent = 0;
				int newPercent = 0;
				while(true){
					int readCount = is.read(buf);
					if(readCount <= 0){
						publishProgress(100);
						break;
					}
					fos.write(buf, 0, readCount);
					finishSize += readCount;
					newPercent = (int)(((long)finishSize*100)/fileSize);
					if(newPercent > oldPercent){
						publishProgress(newPercent);
						oldPercent = newPercent;
					}
				}
				return apkFile.getAbsolutePath();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}finally{
				try {
					if(fos != null)
						fos.close();
					if(is != null)
						is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			int percent = values[0];
			if(percent < 100){
				String progressString = String.format(getString(R.string.downloading), percent);
				notification.contentView.setProgressBar(R.id.notification_progress, 100, percent, false);
				notification.contentView.setTextViewText(R.id.notification_text, progressString);
				manager.notify(DOWNLOAD_NOTY_ID, notification);
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			manager.cancel(DOWNLOAD_NOTY_ID);
			if(!TextUtils.isEmpty(result)){
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setDataAndType(Uri.parse("file://" + result), INSTALL_TYPE);
				startActivity(intent);
			}
			stopSelf();
		}
	}
	
	private void initNotification(){
		notification = new Notification();
		notification.icon = R.drawable.ic_launcher;
		notification.contentView = new RemoteViews(
				getApplicationContext().getPackageName(), R.layout.notification_loading_layout);
		notification.contentView.setProgressBar(R.id.notification_progress, 100, 0, false);
		String progressString = String.format(getString(R.string.downloading), 0);
		notification.tickerText = progressString;
		notification.contentView.setTextViewText(R.id.notification_text, progressString);
		manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(DOWNLOAD_NOTY_ID, notification);
	}
	
	private File getDownloadFile(){
		FileOutputStream fos = null;
		try {
			File apkFile = null;
			if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
				apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),DOWNLOAD_FILE_NAME);
			}else {
				fos = openFileOutput(DOWNLOAD_FILE_NAME, Context.MODE_PRIVATE);
				apkFile = getFileStreamPath(DOWNLOAD_FILE_NAME);
			}
			return apkFile;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
