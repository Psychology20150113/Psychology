package com.dcy.psychology.fragment;

import com.dcy.psychology.GrowLevelChooseActivity;
import com.dcy.psychology.ProblemDirectoryActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.ShowListActivity;
import com.dcy.psychology.util.Constants;

import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TabCureFragment extends Fragment implements OnClickListener{
	private Context mContext;
	private View chooseLayout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_two_layout, null);
		view.findViewById(R.id.problem_one_rl).setOnClickListener(this);
		view.findViewById(R.id.problem_two_rl).setOnClickListener(this);
		chooseLayout = view.findViewById(R.id.choose_ll);
		view.findViewById(R.id.student_grow_ll).setOnClickListener(this);
		view.findViewById(R.id.student_test_ll).setOnClickListener(this);
		view.findViewById(R.id.student_pic_ll).setOnClickListener(this);
//		view.findViewById(R.id.problem_three_tv).setOnClickListener(this);
		return view;
	}
	
	@Override
	public void onClick(View view) {
//		int problemIndex = 0;
//		Intent mIntent = new Intent(mContext, ProblemDirectoryActivity.class);
		switch (view.getId()) {
		case R.id.problem_one_rl:
			chooseLayout.setVisibility(View.VISIBLE);
			chooseLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_from_right));
//			problemIndex = 0;
//			mIntent.putExtra("problem_index", problemIndex);
//			startActivity(mIntent);
			break;
		case R.id.student_grow_ll:
			Intent growIntent = new Intent(mContext, GrowLevelChooseActivity.class);
			growIntent.putExtra(Constants.ThemeIndex, 0);
			growIntent.putExtra(Constants.IsSpecial, true);
			startActivity(growIntent);
			break;
		case R.id.student_pic_ll:
			break;
		case R.id.student_test_ll:
			Intent mIntent = new Intent(mContext, ShowListActivity.class);
			mIntent.putExtra(Constants.ThemeTitle, getString(R.string.problem_introduce_two));
			mIntent.putExtra(Constants.ThemeIndex, 0);
			mIntent.putExtra(Constants.ListType, Constants.SpecialTestType);
			startActivity(mIntent);
			break;
		case R.id.problem_two_rl:
			Toast.makeText(mContext, R.string.please_wait, Toast.LENGTH_SHORT).show();
			if(chooseLayout.getVisibility() == View.VISIBLE){
				Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_to_left);
				animation.setAnimationListener(mAnimationListener);
				chooseLayout.startAnimation(animation);
			}
//			problemIndex = 1;
			break;
//		case R.id.problem_three_tv:
//			problemIndex = 2;
//			break;
		default:
			break;
		}
	}
	
	private AnimationListener mAnimationListener = new AnimationListener() {
		@Override
		public void onAnimationStart(Animation animation) {
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			chooseLayout.setVisibility(View.GONE);
		}
	};
}
