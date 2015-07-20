package com.dcy.psychology.xinzeng;

import com.dcy.psychology.BaseActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.fragment.UnderwayFragment;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


public class Underway extends BaseActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.underway);
        setTopTitle(R.string.underway);
        
        
        //�ڳ����м���Fragment
    	/*FragmentTransaction transaction = getFragmentManager().beginTransaction();
        
        transaction.add(R.id.linear,new Underway());
        transaction.commit();*/
    }

}