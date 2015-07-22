package com.dcy.psychology.xinzeng;

import com.dcy.psychology.BaseActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.fragment.UnderwayFragment;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


public class UnderwayActivity extends BaseActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_underway_layout);
        setTopTitle(R.string.underway);
        
        
    }

}