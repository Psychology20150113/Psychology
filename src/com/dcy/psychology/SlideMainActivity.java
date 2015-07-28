package com.dcy.psychology;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.dcy.psychology.R;
import com.dcy.psychology.adapter.SlideAdapter;
import com.dcy.psychology.fragment.CareerPlanFragment;
import com.dcy.psychology.fragment.ChatIMFragment;
import com.dcy.psychology.fragment.StyleTwoBoxFragment;
import com.dcy.psychology.fragment.StyleTwoMainFragment;
import com.dcy.psychology.fragment.TabChatFragment;
import com.dcy.psychology.fragment.WaitingFragment;
import com.dcy.psychology.fragment.CareerAdviceFragment;
import com.dcy.psychology.util.AsyncImageCache;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.IMManager;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.xinzeng.HelpActivity;
import com.dcy.psychology.xinzeng.PersonalHomepage;
import com.umeng.analytics.MobclickAgent;

public class SlideMainActivity extends BaseActivity implements OnClickListener{
	DrawerLayout drawerLayout;
	private TextView nameText;
	private TextView loginOutText;
	private View nameInfoLayout;
	
	private ViewPager mViewPager;
	private List<Fragment> dataFragment = new ArrayList<Fragment>();
	private RadioGroup mTabRadio;
	private String[] mTabNameArray;
	private AsyncImageCache mCache;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTopTitle(R.string.app_name);
		setContentView(R.layout.activity_slide_main_layout);
		mTabNameArray = mResources.getStringArray(R.array.tab_name);
		mCache = AsyncImageCache.from(this);
		initData();
		initView();
		registerReceiver(mLoginReceiver, new IntentFilter(Constants.ReceiverAction_LoginSuccess));
//		getFragmentManager().beginTransaction().add(R.id.container, new StyleTwoMainFragment()).commit();
//		getFragmentManager().beginTransaction().add(R.id.container, new SlideMainFragment()).commit();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mLoginReceiver);
	}
	
	private void initData(){
	//dataFragment.add(new ChatIMFragment());
	//dataFragment.add(new TabChatFragment());
	//dataFragment.add(new StyleTwoMainFragment());
		dataFragment.add(new CareerAdviceFragment());
		dataFragment.add(new CareerPlanFragment());
		dataFragment.add(new StyleTwoBoxFragment());
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		for(int i=0 ; i<dataFragment.size() ; i++){
			transaction.add(R.id.main_vp, dataFragment.get(i));
			transaction.hide(dataFragment.get(i));
		}
		transaction.commit();
	}
	
	private void initView() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		nameText = (TextView) findViewById(R.id.name_tv);
		nameInfoLayout = findViewById(R.id.rl_name_info);
		nameInfoLayout.setOnClickListener(this);
		loginOutText = (TextView)findViewById(R.id.tv_loginout);
		loginOutText.setOnClickListener(this);
		if(!TextUtils.isEmpty(MyApplication.myHeadUrl)){
			mCache.displayImage((ImageView)findViewById(R.id.iv_header), R.drawable.ic_launcher, 
					new AsyncImageCache.NetworkImageGenerator(MyApplication.myHeadUrl, MyApplication.myHeadUrl));
		}
		findViewById(R.id.iv_header).setOnClickListener(this);
		findViewById(R.id.ll_slide_attention).setOnClickListener(this);
		findViewById(R.id.ll_slide_dna).setOnClickListener(this);
		findViewById(R.id.ll_slide_message).setOnClickListener(this);
		findViewById(R.id.ll_slide_problem).setOnClickListener(this);
		findViewById(R.id.ll_help).setOnClickListener(this);
		setLeftView(R.drawable.icon_slide);
		if(!TextUtils.isEmpty(MyApplication.myPhoneNum)){
			loginOutText.setVisibility(View.VISIBLE);
			nameText.setText(MyApplication.myPhoneNum);
		}
		mViewPager = (ViewPager) findViewById(R.id.main_vp);
		mViewPager.setAdapter(new Utils.MainTabAdapter(getFragmentManager(), dataFragment));
		mViewPager.setOnPageChangeListener(mPageListener);
		mTabRadio = (RadioGroup) findViewById(R.id.tab_rg);
		mTabRadio.setOnCheckedChangeListener(mCheckedChangeListener);
		mViewPager.setCurrentItem(1);
	}
	
	private BroadcastReceiver mLoginReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			if(Constants.ReceiverAction_LoginSuccess.equals(intent.getAction())){
				nameText.setText(MyApplication.myPhoneNum);
				loginOutText.setVisibility(View.VISIBLE);
			}
		}
	};
	
	private OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			int position = 0;
			switch (checkedId) {
				case R.id.tab_one_rb:
					position = 0;
					break;
				case R.id.tab_two_rb:
					position = 1;
					break;
				case R.id.tab_three_rb:
					position = 2;
					break;
				default:
					break;
			}
			setTopTitle(mTabNameArray[position]);
			mViewPager.setCurrentItem(position);
		}
	};
	
	private OnPageChangeListener mPageListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			setTopTitle(mTabNameArray[position]);
			mTabRadio.check(getCheckedBtnId(position));
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	private int getCheckedBtnId(int position){
		int id = 0;
		switch (position) {
			case 0:
				id = R.id.tab_one_rb;
				break;
			case 1:
				id = R.id.tab_two_rb;
				break;
			case 2:
				id = R.id.tab_three_rb;
				break;
			default:
				break;
		}
		return id;
	}
	
	@Override
	public void onLeftViewClick() {
		//drawerLayout.openDrawer(Gravity.LEFT);
			if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
			
				drawerLayout.closeDrawer(Gravity.LEFT);
			}
			else{
				drawerLayout.openDrawer(Gravity.LEFT);
			}
		}
	
	@Override
	public void onRightViewClick() {
		startActivity(new Intent(this, ChatIMActivity.class));
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.tv_loginout){
			clearInfo();
			IMManager.getInstance().logoutIM();
			nameText.setText(R.string.login_now);
			loginOutText.setVisibility(View.GONE);
			if(!TextUtils.isEmpty(MyApplication.myHeadUrl)){
				((ImageView)findViewById(R.id.iv_header)).setImageResource(R.drawable.icon_slide_default_header);
			}
			return;
		} else if(v.getId() == R.id.rl_name_info){
			if(TextUtils.isEmpty(MyApplication.myPhoneNum)){
				Intent loginIntent = new Intent(this , LoginActivity.class);
				if(new InfoShared(this).lastIsDoctor()){
					loginIntent.putExtra(Constants.UserRole, Constants.RoleTeacher);
				}
				startActivityForResult(loginIntent, 0);
			}
			return;
		}
		Intent mIntent = null;
		switch (v.getId()) {
		case R.id.iv_header:
			if(TextUtils.isEmpty(MyApplication.myPhoneNum)){
				mIntent = new Intent(this, LoginActivity.class);
			} else {
				mIntent = new Intent(this, PersonalHomepage.class);
			}
			break;
		case R.id.ll_slide_attention:
			if(TextUtils.isEmpty(MyApplication.myPhoneNum)){
				mIntent = new Intent(this, LoginActivity.class);
			} else {
				mIntent = new Intent(this, GetFollowUsersActivity.class);
			}
			break;
		case R.id.ll_slide_dna:
			if(TextUtils.isEmpty(MyApplication.myPhoneNum)){
				mIntent = new Intent(this, LoginActivity.class);
			} else {
				mIntent = new Intent(this, MineDnaActivity.class);
			}
			break;
		case R.id.ll_slide_message:
			if(TextUtils.isEmpty(MyApplication.myPhoneNum)){
				mIntent = new Intent(this, LoginActivity.class);
			} else {
				mIntent = new Intent(this, PersonalInfoActivity.class);
			}
			break;
		case R.id.ll_slide_problem:
			mIntent = new Intent(this, ShowProblemActivity.class);
			break;
		case R.id.ll_help:
			mIntent = new Intent(this, HelpActivity.class);
			break;
		default:
			break;
		}
		if(mIntent != null){
			startActivity(mIntent);
		}
	}
	
	private void clearInfo(){
		MyApplication.myUserName = "";
		MyApplication.myPwd = "";
		MyApplication.myNick = "";
		MyApplication.myPhoneNum = "";
		new InfoShared(this).clearInfo();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case 0:
			if(data == null){
				return;
			}
			loginOutText.setVisibility(View.VISIBLE);
			nameText.setText(MyApplication.myPhoneNum);
			break;
		default:
			break;
		}
	}
}
