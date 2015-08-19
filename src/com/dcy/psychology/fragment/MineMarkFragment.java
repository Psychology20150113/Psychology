package com.dcy.psychology.fragment;

import java.util.ArrayList;

import com.dcy.psychology.DetailTestResultActivity;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.dcy.psychology.ShowListActivity;
import com.dcy.psychology.gsonbean.UserInfoBean;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.MyMarkerView;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MineMarkFragment extends Fragment implements OnClickListener{
	private InfoShared mShared;
	private Context mContext;
	private RadarChart mChart;
	private String[] dataArray;
	private String[] careerArray;
	private View rootView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mShared = new InfoShared(mContext);
		careerArray = mContext.getResources().getStringArray(R.array.zhiye_array);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_mine_mark_layout, null);
		rootView.findViewById(R.id.tv_look_info).setOnClickListener(this);
		rootView.findViewById(R.id.tv_test).setOnClickListener(this);
		initZhiyeView();
		initQizhiView();
		if(TextUtils.isEmpty(mShared.getHollendResult()) && TextUtils.isEmpty(mShared.getQizhiResult())){
			new GetInfoTask().execute();
		}
		return rootView;
	}

	private void initZhiyeView() {
		mChart = (RadarChart) rootView.findViewById(R.id.view_chart);
		mChart.setMarkerView(new MyMarkerView(mContext, R.layout.custom_marker_view));
		mChart.setDescription("");
		mChart.getYAxis().setEnabled(false);
		mChart.getLegend().setEnabled(false);
		if(!TextUtils.isEmpty(mShared.getHollendResult())){
			SpannableStringBuilder spanBuilder = new SpannableStringBuilder(String.format(
					getString(R.string.mine_zhiye), mShared.getHollendResult()));
			spanBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.v2_blue)), 4, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			((TextView)rootView.findViewById(R.id.tv_zhiye)).setText(spanBuilder);
			dataArray = mShared.getHollendData().split(",");
			setData();
		} else {
			rootView.findViewById(R.id.tv_empty_zhiye).setVisibility(View.VISIBLE);
			rootView.findViewById(R.id.tv_zhiye).setVisibility(View.GONE);
			mChart.setVisibility(View.GONE);
		}
	}
	
	private void initQizhiView(){
		if(!TextUtils.isEmpty(mShared.getQizhiResult())){
			String[] mQizhiData = mShared.getQizhiData().split(",");
			setQizhiData(mQizhiData);
			SpannableStringBuilder spanBuilder = new SpannableStringBuilder(String.format(getString(R.string.mine_qizhi), mShared.getQizhiResult()));
			spanBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.v2_blue)), 7, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			((TextView)rootView.findViewById(R.id.tv_qizhi)).setText(spanBuilder);
		} else {
			rootView.findViewById(R.id.ll_qizhi_show).setVisibility(View.GONE);
			rootView.findViewById(R.id.tv_empty_qizhi).setVisibility(View.VISIBLE);
		}
	}

	private void setQizhiData(String[] mQizhiData) {
		if(mQizhiData.length != 4){
			return;
		}
		((ProgressBar)rootView.findViewById(R.id.pb_qizhi_one)).setProgress(Integer.parseInt(mQizhiData[0]) * 100 / 15 );
		((ProgressBar)rootView.findViewById(R.id.pb_qizhi_two)).setProgress(Integer.parseInt(mQizhiData[1]) * 100 / 15 );
		((ProgressBar)rootView.findViewById(R.id.pb_qizhi_three)).setProgress(Integer.parseInt(mQizhiData[2]) * 100 / 15);
		((ProgressBar)rootView.findViewById(R.id.pb_qizhi_four)).setProgress(Integer.parseInt(mQizhiData[3]) * 100 / 15 );
		((TextView)rootView.findViewById(R.id.tv_point_one)).setText(mQizhiData[0]);
		((TextView)rootView.findViewById(R.id.tv_point_two)).setText(mQizhiData[1]);
		((TextView)rootView.findViewById(R.id.tv_point_three)).setText(mQizhiData[2]);
		((TextView)rootView.findViewById(R.id.tv_point_four)).setText(mQizhiData[3]);
	}
	
	private class GetInfoTask extends AsyncTask<Void, Void, UserInfoBean> {
		@Override
		protected UserInfoBean doInBackground(Void... params) {
			return Utils.getUserInfo(MyApplication.myPhoneNum);
		}
		
		@Override
		protected void onPostExecute(UserInfoBean result) {
			if(result == null){
				return;
			}
			if(!TextUtils.isEmpty(result.HollendTestSpeciesScores)){
				dataArray = result.HollendTestSpeciesScores.split(",");
				if(dataArray != null && dataArray.length == 6){
					((TextView)rootView.findViewById(R.id.tv_zhiye)).setText(String.format(
							getString(R.string.mine_zhiye), result.HollendTest));
					setData();
					rootView.findViewById(R.id.tv_empty_zhiye).setVisibility(View.GONE);
					rootView.findViewById(R.id.tv_zhiye).setVisibility(View.VISIBLE);
					mChart.setVisibility(View.VISIBLE);
					mShared.setHollendResult(result.HollendTestSpeciesScores, 
							result.HollendTest, "");
				}
			}
			if(!TextUtils.isEmpty(result.TemperamentTestSpeciesScores)){
				String[] qizhiArray = result.TemperamentTestSpeciesScores.split(",");
				if(qizhiArray != null && qizhiArray.length == 4){
					setQizhiData(qizhiArray);
					SpannableStringBuilder spanBuilder = new SpannableStringBuilder(String.format(getString(R.string.mine_qizhi), result.TemperamentTest));
					spanBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), 7, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					((TextView)rootView.findViewById(R.id.tv_qizhi)).setText(spanBuilder);
					rootView.findViewById(R.id.ll_qizhi_show).setVisibility(View.VISIBLE);
					rootView.findViewById(R.id.tv_empty_qizhi).setVisibility(View.GONE);
					mShared.setQizhiResult(result.TemperamentTestSpeciesScores, 
							result.TemperamentTest, "");
				}
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		Intent mIntent = null;
		switch (v.getId()) {
		case R.id.tv_look_info:
			mIntent = new Intent(mContext, DetailTestResultActivity.class);
			mIntent.putExtra(Constants.QizhiResult, mShared.getQizhiResult());
			mIntent.putExtra(Constants.ZhiyeResult, mShared.getHollendResult());
			break;
		case R.id.tv_test:
			((Activity)mContext).finish();
			mIntent = new Intent(mContext, ShowListActivity.class);
			mIntent.putExtra(Constants.ListType, Constants.DNAType);
			break;
		default:
			break;
		}
		startActivity(mIntent);
	}
	
	public void setData() {
	    if(dataArray == null){
	    	return;
	    }
	 	ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        
        for (int i = 0; i < careerArray.length; i++) {
        	yVals1.add(new Entry(Float.valueOf(dataArray[i]), i));
        }
        RadarDataSet set1 = new RadarDataSet(yVals1, "Set 1");
        set1.setColor(getResources().getColor(R.color.v2_blue));
        set1.setDrawFilled(false);
        set1.setLineWidth(5f);//��ɫ�����Ŀ��
        RadarData data = new RadarData(careerArray, set1);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        mChart.setData(data);
        mChart.invalidate();
    }
}
