package com.dcy.psychology;

import com.dcy.psychology.util.Utils;

import android.app.Activity;
import android.os.AsyncTask;
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
			if(TextUtils.isEmpty(mInputEt.getText())){
				Toast.makeText(this, R.string.please_input, Toast.LENGTH_SHORT).show();
				return;
			}
			showCustomDialog();
			new InputTask().execute(mInputEt.getText().toString());
			break;
		default:
			break;
		}
	}
	
	private class InputTask extends AsyncTask<String, Void, Boolean>{
		String inputString;
		
		@Override
		protected Boolean doInBackground(String... params) {
			if(TextUtils.isEmpty(params[0]))
				return null;
			inputString = params[0];
			return Utils.inputBlackHole(params[0]);
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			hideCustomDialog();
			if(result){
				mInputEt.setText("");
				Toast.makeText(BlackHoleActivity.this, String.format(getString(R.string.black_hole_success), inputString), 
						Toast.LENGTH_SHORT).show();
			}else {
				Toast.makeText(BlackHoleActivity.this, getString(R.string.black_hole_failed), Toast.LENGTH_SHORT).show();
			}
		}
	}
}
