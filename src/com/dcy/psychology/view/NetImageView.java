package com.dcy.psychology.view;

import android.content.Context;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;

/**
 * Created by dcy123 on 2015/3/18.
 */
public class NetImageView extends NetworkImageView{

    public NetImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDefaultImageResId(R.drawable.ic_launcher);
        setErrorImageResId(R.drawable.ic_launcher);
    }

    public NetImageView(Context context) {
        this(context, null);
    }

    public void loadUrl(String url){
        setImageUrl(url, MyApplication.getInstance().getNetworkManager().getImageLoader());
    }

    public void loadUrl(String url, int resId){
        setDefaultImageResId(resId);
        setErrorImageResId(resId);
        setImageUrl(url, MyApplication.getInstance().getNetworkManager().getImageLoader());
    }
}
