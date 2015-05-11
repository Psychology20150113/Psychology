package com.dcy.psychology;

import com.dcy.psychology.fragment.OnlineClassListFragment;
import com.dcy.psychology.fragment.OnlinePicFragment;
import com.dcy.psychology.util.Constants;

import android.app.Fragment;
import android.os.Bundle;

public class OnlinePicListActivity extends BaseActivity {
	private int categoryId = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_online_pic_layout);
		categoryId = getIntent().getIntExtra(Constants.ClassCategoryId, -1);
		if(categoryId == -1){
			setTopTitle(R.string.online_pic);
			getFragmentManager().beginTransaction()
				.add(R.id.container_ll, new OnlinePicFragment()).commit();
		} else {
			setTopTitle(getIntent().getStringExtra(Constants.ClassCategoryName));
			Fragment classFragment = new OnlineClassListFragment();
			Bundle bundle = new Bundle();
			bundle.putInt(Constants.ClassCategoryId, categoryId);
			classFragment.setArguments(bundle);
			getFragmentManager().beginTransaction()
				.add(R.id.container_ll, classFragment).commit();
		}
	}
}
