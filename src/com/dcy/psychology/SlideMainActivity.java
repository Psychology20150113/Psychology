package com.dcy.psychology;

import java.util.ArrayList;
import java.util.List;

import com.dcy.psychology.adapter.SlideAdapter;
import com.dcy.psychology.db.DbHelper;
import com.dcy.psychology.db.SqlConstants;
import com.dcy.psychology.fragment.ChatIMFragment;
import com.dcy.psychology.fragment.SlideMainFragment;
import com.dcy.psychology.fragment.StyleTwoMainFragment;
import com.dcy.psychology.fragment.TabCureFragment;
import com.dcy.psychology.fragment.TabGrowthFragment;
import com.dcy.psychology.fragment.TabMineFragment;
import com.dcy.psychology.util.IMManager;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.util.Utils;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SlideMainActivity extends BaseActivity implements OnItemClickListener
		,OnClickListener{
	DrawerLayout drawerLayout;
	private TextView nameText;
	private View nameLayout;
	private View loginLayout;
	
	private ViewPager mViewPager;
	private List<Fragment> dataFragment = new ArrayList<Fragment>();
	private RadioGroup mTabRadio;
	private String[] mTabNameArray;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTopTitle(R.string.app_name);
		setContentView(R.layout.activity_slide_main_layout);
		mTabNameArray = mResources.getStringArray(R.array.tab_name);
		initData();
		initView();
//		getFragmentManager().beginTransaction().add(R.id.container, new StyleTwoMainFragment()).commit();
//		getFragmentManager().beginTransaction().add(R.id.container, new SlideMainFragment()).commit();
	}
	
	private void initData(){
		dataFragment.add(new ChatIMFragment());
		dataFragment.add(new TabCureFragment());
		dataFragment.add(new TabMineFragment());
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
		nameLayout = findViewById(R.id.name_ll);
		loginLayout = findViewById(R.id.login_ll);
		loginLayout.setOnClickListener(this);
		findViewById(R.id.logout_tv).setOnClickListener(this);
		ListView slideView = (ListView) findViewById(R.id.drawer_lv);
		slideView.setAdapter(new SlideAdapter(this));
		slideView.setOnItemClickListener(this);
		setLeftView(R.drawable.icon_user);
		setRightView(R.drawable.ic_launcher);
		if(!TextUtils.isEmpty(MyApplication.myUserName)){
			nameLayout.setVisibility(View.VISIBLE);
			nameText.setText(MyApplication.myUserName);
			loginLayout.setVisibility(View.GONE);
		}
		
		mViewPager = (ViewPager) findViewById(R.id.main_vp);
		mViewPager.setAdapter(new Utils.MainTabAdapter(getFragmentManager(), dataFragment));
		mViewPager.setOnPageChangeListener(mPageListener);
		mTabRadio = (RadioGroup) findViewById(R.id.tab_rg);
		mTabRadio.setOnCheckedChangeListener(mCheckedChangeListener);
	}
	
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
		drawerLayout.openDrawer(Gravity.LEFT);
	}
	
	@Override
	public void onRightViewClick() {
		startActivity(new Intent(this, ChatIMActivity.class));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.logout_tv:
			clearInfo();
			IMManager.getInstance().logoutIM();
			loginLayout.setVisibility(View.VISIBLE);
			nameLayout.setVisibility(View.GONE);
			break;
		case R.id.login_ll:
			startActivityForResult(new Intent(this , LoginActivity.class), 0);
			break;
		default:
			break;
		}
	}
	
	private void clearInfo(){
		MyApplication.myUserName = "";
		MyApplication.myPwd = "";
		MyApplication.myNick = "";
		new InfoShared(this).clearInfo();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent mIntent;
		switch (position) {
		case 0:
			mIntent = new Intent(this, BlackHoleActivity.class);
			startActivity(mIntent);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case 0:
			nameLayout.setVisibility(View.VISIBLE);
			nameText.setText(MyApplication.myUserName);
			loginLayout.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}
}
