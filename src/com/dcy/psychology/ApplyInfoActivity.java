package com.dcy.psychology;

import com.dcy.psychology.gsonbean.ApplyInfoBean;
import com.dcy.psychology.view.NetImageView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

public class ApplyInfoActivity extends BaseActivity {
	private ApplyInfoBean itemBean;
	private int[] bgRes = {R.drawable.bg_tv_applying, R.drawable.bg_tv_apply_success, 
			R.drawable.bg_tv_busy};
	private String[] colorString = {"#ec833c", "#333638", "#898989"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_apply_info_layout);
		itemBean = (ApplyInfoBean) getIntent().getSerializableExtra("apply_info");
		initView();
	}
	
	private void initView(){
		if(itemBean == null){
			return;
		}
		((NetImageView) findViewById(R.id.iv_head)).loadUrl(itemBean.userheadurl);
		findViewById(R.id.view_tag).setBackgroundDrawable(
				new ColorDrawable(Color.parseColor(colorString[itemBean.state])));
		((TextView) findViewById(R.id.tv_name)).setText(itemBean.username);
		((TextView) findViewById(R.id.tv_item_achieve)).setText(itemBean.UserAchievement);
		findViewById(R.id.tv_statue).setBackgroundResource(bgRes[itemBean.state]);
		findViewById(R.id.tv_top_question).setBackgroundResource(bgRes[itemBean.state]);
		findViewById(R.id.tv_top_introduce).setBackgroundResource(bgRes[itemBean.state]);
		findViewById(R.id.tv_top_time).setBackgroundResource(bgRes[itemBean.state]);
//		((TextView) findViewById(R.id.tv_question)).setText(itemBean.)
		
	}
	
}
