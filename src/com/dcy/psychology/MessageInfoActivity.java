package com.dcy.psychology;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.dcy.psychology.gsonbean.UserInfoBean;
import com.dcy.psychology.util.AsyncImageCache;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.Utils;

public class MessageInfoActivity extends BaseActivity implements OnClickListener{
	private String phoneNum;
	private TextView nameTv;
	private ImageView headerView;
	private TextView achieveView;
	private TextView mInfoView;
	private AsyncImageCache mAsyncImageCache;
	private Context mContext;
	public Button mbutton1;
	public Button mbutton2;
	private long specialId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_info_layout);
		
		mContext = this;
		mAsyncImageCache = AsyncImageCache.from(this);
		initView();
		phoneNum = getIntent().getStringExtra(Constants.PhoneNum);
		if(!TextUtils.isEmpty(phoneNum)){
			showCustomDialog();
			new GetInfoTask().execute();
		
		}
	}
	
	@Override
	public void onClick(View v) {
		/*showCustomDialog();
		switch (v.getId()) {
		case R.id.top_right_iv2:
			new GetMatchTask().execute(specialId);
			break;
		case R.id.top_right_iv1:
			if(mContext.getResources().getString(R.string.attention).equals(((TextView)v).getText())){
				new FollowTask((TextView)v, false).execute(specialId);
			} else {
				new FollowTask((TextView)v, true).execute(specialId);
			}
			break;
		default:
			break;
		}*/
		
	}
	
	
	
	private void initView(){
		nameTv = (TextView) findViewById(R.id.tv_item_name);
		headerView = (ImageView) findViewById(R.id.iv_header);
		achieveView = (TextView) findViewById(R.id.tv_item_achieve);
		mInfoView = (TextView) findViewById(R.id.tv_message_info);
		mbutton1=(Button) findViewById(R.id.btn_organiz_data);
		mbutton2=(Button) findViewById(R.id.btn_goto_talk);
	}
	
	private class GetInfoTask extends AsyncTask<Void, Void, UserInfoBean> {
		@Override
		protected UserInfoBean doInBackground(Void... params) {
			return Utils.getUserInfo(phoneNum);
		}
		
		@Override
		protected void onPostExecute(UserInfoBean result) {
			hideCustomDialog();
			if(result == null){
				return;
			}
			specialId = result.UserID;
			setInfoData(result);
		}
	}
	
	
	private void setInfoData(UserInfoBean result){
		nameTv.setText(result.UserName);
		achieveView.setText(result.UserAchievement);
		mAsyncImageCache.displayImage(headerView, R.drawable.ic_launcher, 
				new AsyncImageCache.NetworkImageGenerator(result.UserHeadUrl, result.UserHeadUrl));
		mInfoView.setText(result.UserResume);
		//通过判断消息的类型来决定是否显示完善资料和进入约聊室两个按钮，即选择通知的显示类型
		if(result.UserID == 2000000205)
		{
		mbutton1.setVisibility(View.VISIBLE);
		}
		
		if(result.UserID == 2000000177)
		{
			mbutton2.setVisibility(View.VISIBLE);
		}
		//mbutton2.setVisibility(View.VISIBLE);

		setTopTitle(result.UserName);
		
	}
	
}
