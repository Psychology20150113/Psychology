package com.dcy.psychology.view;

import com.dcy.psychology.R;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class CustomProgressDialog extends Dialog {
	/**
	 * ��Ϣ�����.
	 */
	TextView tvMessage;

	public CustomProgressDialog(Context context) {
		super(context, R.style.CustomProgressDialog);
		toInit();
	}

	/**
	 * ��ʼ��.
	 */
	private void toInit() {
		// ȥ������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.progress_dialog);

		tvMessage = (TextView) findViewById(android.R.id.message);

		// ���öԻ���͸���ȺͿ��
		DisplayMetrics displayMetrics = getContext().getResources()
				.getDisplayMetrics();
		Window window = getWindow();
		LayoutParams attributes = window.getAttributes();
		attributes.alpha = 0.7f;
		attributes.width = displayMetrics.widthPixels / 2;
		attributes.height = attributes.width;
		window.setAttributes(attributes);
		setCanceledOnTouchOutside(false);
	}

	/**
	 * ���������Ϣ.
	 * 
	 * @param message
	 *            ��Ϣ����.
	 */
	public void setMessage(CharSequence message) {
		tvMessage.setText(message);
	}

	public void setMessage(int id) {
		tvMessage.setText(id);
	}
}
