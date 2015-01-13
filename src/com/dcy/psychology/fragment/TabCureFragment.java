package com.dcy.psychology.fragment;

import com.dcy.psychology.ProblemDirectoryActivity;
import com.dcy.psychology.R;

import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabCureFragment extends Fragment implements OnClickListener{
	private Context mContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_two_layout, null);
		view.findViewById(R.id.problem_one_tv).setOnClickListener(this);
		view.findViewById(R.id.problem_two_tv).setOnClickListener(this);
		view.findViewById(R.id.problem_three_tv).setOnClickListener(this);
		return view;
	}
	
	@Override
	public void onClick(View view) {
		int problemIndex = 0;
		Intent mIntent = new Intent(mContext, ProblemDirectoryActivity.class);
		switch (view.getId()) {
		case R.id.problem_one_tv:
			problemIndex = 0;
			break;
		case R.id.problem_two_tv:
			problemIndex = 1;
			break;
		case R.id.problem_three_tv:
			problemIndex = 2;
			break;
		default:
			break;
		}
		mIntent.putExtra("problem_index", problemIndex);
		startActivity(mIntent);
	}
}
