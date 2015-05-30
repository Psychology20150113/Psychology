package com.dcy.psychology;

import com.dcy.psychology.util.Constants;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MineActivity extends BaseActivity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_layout);
		setTopTitle(R.string.mine);
		findViewById(R.id.tv_attention).setOnClickListener(this);
		findViewById(R.id.tv_dna).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		Intent mIntent = null;
		switch (v.getId()) {
		case R.id.tv_attention:
//			mIntent = new Intent(this, );
			break;
		case R.id.tv_dna:
			mIntent = new Intent(this, ShowListActivity.class);
			mIntent.putExtra(Constants.ListType, Constants.DNAType);
			break;
		default:
			break;
		}
		if(mIntent != null){
			startActivity(mIntent);
		}
	}
}
