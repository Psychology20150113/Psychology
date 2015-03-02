package com.dcy.psychology;

import com.dcy.psychology.adapter.SlideAdapter;
import com.dcy.psychology.fragment.SlideMainFragment;
import com.dcy.psychology.util.InfoShared;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class SlideMainActivity extends BaseActivity implements OnItemClickListener
		,OnClickListener{
	DrawerLayout drawerLayout;
	private TextView nameText;
	private View nameLayout;
	private View loginLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTopTitle(R.string.app_name);
		setContentView(R.layout.activity_slide_main_layout);
		initView();
		getFragmentManager().beginTransaction().add(R.id.container, new SlideMainFragment()).commit();
	}

	private void initView() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		nameText = (TextView) findViewById(R.id.name_tv);
		nameLayout = findViewById(R.id.name_ll);
		loginLayout = findViewById(R.id.login_ll);
		loginLayout.setOnClickListener(this);
		findViewById(R.id.logout_tv).setOnClickListener(this);
		ListView slideView = (ListView) findViewById(R.id.drawer_lv);
		slideView.setAdapter(new SlideAdapter(this));
		slideView.setOnItemClickListener(this);
		setLeftView(R.drawable.icon_user);
		if(!TextUtils.isEmpty(MyApplication.myUserName)){
			nameLayout.setVisibility(View.VISIBLE);
			nameText.setText(MyApplication.myUserName);
			loginLayout.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onLeftViewClick() {
		drawerLayout.openDrawer(Gravity.LEFT);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.logout_tv:
			clearInfo();
			loginLayout.setVisibility(View.VISIBLE);
			nameLayout.setVisibility(View.GONE);
			break;
		case R.id.login_ll:
			startActivityForResult(new Intent(this , LoginActivity.class), 0);
			break;
		default:
			break;
		}
	}
	
	private void clearInfo(){
		MyApplication.myUserName = "";
		MyApplication.myPwd = "";
		MyApplication.myNick = "";
		new InfoShared(this).clearInfo();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent mIntent;
		switch (position) {
		case 0:
			mIntent = new Intent(this, BlackHoleActivity.class);
			startActivity(mIntent);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case 0:
			nameLayout.setVisibility(View.VISIBLE);
			nameText.setText(MyApplication.myUserName);
			loginLayout.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}
}
