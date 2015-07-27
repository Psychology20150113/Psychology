package com.dcy.psychology.network;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by dcy123 on 2015/3/18.
 */
public class BitmapCache implements ImageLoader.ImageCache{
    private LruCache<String, Bitmap> mCache;
    private  int CacheSize;

    public BitmapCache(){
        CacheSize= (int)Runtime.getRuntime().maxMemory()/10;
        mCache = new LruCache<String, Bitmap>(CacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }
    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mCache.put(url, bitmap);
    }
}
