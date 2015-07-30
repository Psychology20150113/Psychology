package com.dcy.psychology.xinzeng;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.dcy.psychology.BaseActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.fragment.AppliedFragment;
import com.dcy.psychology.fragment.GetFollowUsersFragmentActivity;
import com.dcy.psychology.fragment.MatchestSpecicalUserFragment;
import com.dcy.psychology.fragment.MineMarkFragment;
import com.dcy.psychology.util.Utils;

public class MineDoctorActivity extends BaseActivity{
	private RadioGroup tabRg;
	private ViewPager mViewPager;
	private ArrayList<Fragment> dataFragment = new ArrayList<Fragment>();
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_doctor_layout);
		setTopTitle(R.string.mine_doctor);
		
		initView();
	}

	private void initView() {
		dataFragment.add(new GetFollowUsersFragmentActivity());
		dataFragment.add(new AppliedFragment());
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		for(int i=0 ; i<dataFragment.size() ; i++){
			transaction.add(R.id.vp_mine_doctor, dataFragment.get(i));
			transaction.hide(dataFragment.get(i));
		}
		transaction.commit();
		tabRg = (RadioGroup) findViewById(R.id.rg_tab);
		tabRg.setOnCheckedChangeListener(mCheckedChangeListener);
		mViewPager = (ViewPager) findViewById(R.id.vp_mine_doctor);
		mViewPager.setOnPageChangeListener(mChangeListener);
		mViewPager.setAdapter(new Utils.MainTabAdapter(getFragmentManager(), dataFragment));
	}
	
	private OnPageChangeListener mChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			if(position == 0){
				tabRg.check(R.id.rb_concerned);
			} else {
				tabRg.check(R.id.rb_applied);
			}
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}
	};
	
	private OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if(checkedId == R.id.rb_concerned){
				mViewPager.setCurrentItem(0);
			} else {
				mViewPager.setCurrentItem(1);
			}
		}
	};

}
