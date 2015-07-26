package com.dcy.psychology;

import org.json.JSONObject;

import com.dcy.psychology.R;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.Utils;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

public class DetailTestResultActivity extends BaseActivity {
	private String zhiyeResult;
	private String qizhiResult;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_test_result_layout);
		setTopTitle(R.string.detail_info);
		zhiyeResult = getIntent().getStringExtra(Constants.ZhiyeResult);
		qizhiResult = getIntent().getStringExtra(Constants.QizhiResult);
		initZhiyeText();
		initQizhiText();
	}
	
	private void initZhiyeText(){
		if(TextUtils.isEmpty(zhiyeResult)){
			return;
		}
		try {
			StringBuilder resultBuilder = new StringBuilder();
			resultBuilder.append(String.format(getString(R.string.mine_zhiye), zhiyeResult)).append("\n\n");
			JSONObject jsonObject = new JSONObject(Utils.loadRawString(this, R.raw.result_zhiye));
			for(int i = 0 ; i < zhiyeResult.length() ; i ++){
				resultBuilder.append(jsonObject.getString(String.valueOf(zhiyeResult.charAt(i)))).append("\n\n");
			}
			((TextView) findViewById(R.id.tv_result_zhiye)).setText(resultBuilder.substring(0, resultBuilder.length() - 2));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initQizhiText(){
		if(TextUtils.isEmpty(qizhiResult)){
			return;
		}
		try {
			StringBuilder resultBuilder = new StringBuilder();
			resultBuilder.append(String.format(getString(R.string.mine_zhiye), qizhiResult)).append("\n\n");
			JSONObject jsonObject = new JSONObject(Utils.loadRawString(this, R.raw.result_qizhi));
			resultBuilder.append(jsonObject.getString(qizhiResult));
			((TextView) findViewById(R.id.tv_result_qizhi)).setText(resultBuilder.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
