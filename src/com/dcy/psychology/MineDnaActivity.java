package com.dcy.psychology;

import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.fragment.MatchestSpecicalUserFragment;
import com.dcy.psychology.fragment.MineMarkFragment;
import com.dcy.psychology.util.Utils;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MineDnaActivity extends BaseActivity {
	
	private RadioGroup tabRg;
	private ViewPager mViewPager;
	private ArrayList<Fragment> dataFragment = new ArrayList<Fragment>();
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_dna_layout);
		setTopTitle(R.string.mine_dna);
		
		initView();
	}

	private void initView() {
		dataFragment.add(new MineMarkFragment());
		dataFragment.add(new MatchestSpecicalUserFragment());
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		for(int i=0 ; i<dataFragment.size() ; i++){
			transaction.add(R.id.vp_mine_dna, dataFragment.get(i));
			transaction.hide(dataFragment.get(i));
		}
		transaction.commit();
		tabRg = (RadioGroup) findViewById(R.id.rg_tab);
		tabRg.setOnCheckedChangeListener(mCheckedChangeListener);
		mViewPager = (ViewPager) findViewById(R.id.vp_mine_dna);
		mViewPager.setOnPageChangeListener(mChangeListener);
		mViewPager.setAdapter(new Utils.MainTabAdapter(getFragmentManager(), dataFragment));
	}
	
	private OnPageChangeListener mChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			if(position == 0){
				tabRg.check(R.id.rb_mark);
			} else {
				tabRg.check(R.id.rb_match);
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
			if(checkedId == R.id.rb_mark){
				mViewPager.setCurrentItem(0);
			} else {
				mViewPager.setCurrentItem(1);
			}
		}
	};
	
}
