package com.dcy.psychology.fragment;

import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.util.Utils;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class TabChatFragment extends Fragment {
	private RadioGroup tabRg;
	private ViewPager mViewPager;
	private ArrayList<Fragment> dataFragment = new ArrayList<Fragment>();
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataFragment.add(new NewChatIMFragment());
		dataFragment.add(new NewChatIMFragment());
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		for(int i=0 ; i<dataFragment.size() ; i++){
			transaction.add(R.id.vp_chat, dataFragment.get(i));
			transaction.hide(dataFragment.get(i));
		}
		transaction.commit();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tab_chat_layout, null);
		tabRg = (RadioGroup) view.findViewById(R.id.rg_tab);
		tabRg.setOnCheckedChangeListener(mCheckedChangeListener);
		mViewPager = (ViewPager) view.findViewById(R.id.vp_chat);
		mViewPager.setOnPageChangeListener(mChangeListener);
		mViewPager.setAdapter(new Utils.MainTabAdapter(getFragmentManager(), dataFragment));
		return view;
	}
	
	private OnPageChangeListener mChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			if(position == 0){
				tabRg.check(R.id.rb_chat);
			} else {
				tabRg.check(R.id.rb_discuss);
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
			if(checkedId == R.id.rb_chat){
				mViewPager.setCurrentItem(0);
			} else {
				mViewPager.setCurrentItem(1);
			}
		}
	};
}
