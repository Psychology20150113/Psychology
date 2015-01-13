package com.dcy.psychology;

import com.dcy.psychology.adapter.SlideAdapter;
import com.dcy.psychology.fragment.SlideMainFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class SlideMainActivity extends BaseActivity implements OnItemClickListener{
	DrawerLayout drawerLayout;
	private TextView nameText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTopTitle(R.string.app_name);
		setContentView(R.layout.activity_slide_main_layout);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		nameText = (TextView) findViewById(R.id.name_tv);
		if(!TextUtils.isEmpty(MyApplication.myUserName)){
			nameText.setVisibility(View.VISIBLE);
			nameText.setText(MyApplication.myUserName);
		}
		ListView slideView = (ListView) findViewById(R.id.drawer_lv);
		slideView.setAdapter(new SlideAdapter(this));
		slideView.setOnItemClickListener(this);
		setLeftView(R.drawable.icon_user);
		getFragmentManager().beginTransaction().add(R.id.container, new SlideMainFragment()).commit();
	}
	
	@Override
	public void onLeftViewClick() {
		drawerLayout.openDrawer(Gravity.LEFT);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent mIntent;
		switch (position) {
		case 0:
			mIntent = new Intent(this , LoginActivity.class);
			startActivityForResult(mIntent, 0);
			break;
		case 1:
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
			nameText.setVisibility(View.VISIBLE);
			nameText.setText(MyApplication.myUserName);
			break;

		default:
			break;
		}
	}
}
