package com.dcy.psychology;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.dcy.psychology.gsonbean.GrowPictureBean;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.Utils;

import android.content.res.AssetManager;
import android.content.res.Resources.NotFoundException;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PlamPictureDetailActivity extends BaseActivity {
	private ImageView mPicView;
	private TextView mContentText;
	private AssetManager manager;
	private GrowPictureBean detailBean;
	
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
			loadRes();
		}
	}
	
	private void loadRes(){
		try {
			InputStream stream = manager.open(detailBean.getPicture());
			mPicView.setImageBitmap(BitmapFactory.decodeStream(stream));
			mContentText.setText(Utils.loadRawString(this, getResources().getIdentifier(detailBean.getContent(), "raw", getPackageName())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
