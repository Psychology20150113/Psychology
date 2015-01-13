package com.dcy.psychology;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class BlackHoleActivity extends BaseActivity implements OnClickListener{
	private EditText mInputEt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_black_hole_layout);
		setTopTitle(R.string.black_hole);
		mInputEt = (EditText) findViewById(R.id.input_et);
		findViewById(R.id.throw_tv).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.throw_tv:
			if(TextUtils.isEmpty(mInputEt.getText()))
				Toast.makeText(this, R.string.please_input, Toast.LENGTH_SHORT).show();
			Toast.makeText(this, mInputEt.getText().toString() + "ËÍÈëºÚ¶´³É¹¦", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}
}
