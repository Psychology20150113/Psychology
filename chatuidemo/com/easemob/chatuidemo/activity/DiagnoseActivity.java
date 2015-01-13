package com.easemob.chatuidemo.activity;

import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.dcy.psychology.R;
import com.easemob.util.EMLog;

/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
/**
 * 璇婃柇鐣岄潰锛涘湪姝や笂浼犻敊璇棩蹇�
 * 
 * @author lyuzhao
 * 
 */
public class DiagnoseActivity extends BaseActivity implements OnClickListener {
	private TextView currentVersion;
	private Button uploadLog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diagnose);

		currentVersion = (TextView) findViewById(R.id.tv_version);
		uploadLog = (Button) findViewById(R.id.button_uploadlog);
		uploadLog.setOnClickListener(this);
		String strVersion = "";
		try {
			strVersion = getVersionName();
		} catch (Exception e) {
		}
		if (!TextUtils.isEmpty(strVersion))
			currentVersion.setText("V" + strVersion);
		else
			currentVersion.setText("鏈缃�");
	}

	public void back(View view) {
		finish();
	}

	private String getVersionName() throws Exception {
		// 鑾峰彇packagemanager鐨勫疄渚�
		PackageManager packageManager = getPackageManager();
		// getPackageName()鏄綘褰撳墠绫荤殑鍖呭悕锛�0浠ｈ〃鏄幏鍙栫増鏈俊鎭�
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
				0);
		String version = packInfo.versionName;
		return version;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_uploadlog:
			uploadlog();
			break;

		default:
			break;
		}

	}

	private ProgressDialog progressDialog;

	public void uploadlog() {

		if (progressDialog == null)
			progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("涓婁紶鏃ュ織涓�...");
		progressDialog.setCancelable(false);
		progressDialog.show();

		EMChat.getInstance().uploadLog(new EMCallBack() {

			@Override
			public void onSuccess() {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						progressDialog.dismiss();
						Toast.makeText(DiagnoseActivity.this, "鏃ュ織涓婁紶鎴愬姛",
								Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onProgress(final int progress, String status) {
				// getActivity().runOnUiThread(new Runnable() {
				//
				// @Override
				// public void run() {
				// progressDialog.setMessage("涓婁紶涓� "+progress+"%");
				//
				// }
				// });

			}

			@Override
			public void onError(int code, String message) {
				EMLog.e("###", message);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						progressDialog.dismiss();
						Toast.makeText(DiagnoseActivity.this, "log涓婁紶澶辫触",
								Toast.LENGTH_SHORT).show();
					}
				});

			}
		});

	}

}
