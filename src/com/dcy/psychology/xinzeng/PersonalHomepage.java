package com.dcy.psychology.xinzeng;

import com.dcy.psychology.BaseActivity;
import com.dcy.psychology.PersonalInfoActivity;
import com.dcy.psychology.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


public class PersonalHomepage extends BaseActivity implements OnClickListener
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_homepage_layout);
        setTopTitle(R.string.personal_homepage);
        findViewById(R.id.ll_edit).setOnClickListener(this);
        
    }

    @Override
   	public void onClick(View v) {
   		Intent mIntent = null;
   			mIntent = new Intent(this, PersonalInfoActivity.class);
   		
   			startActivity(mIntent);
   		
   	}
}