package com.dcy.psychology.network;

import java.io.File;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by dcy123 on 2015/3/18.
 */
public class NetworkManager {
    private static NetworkManager mManager;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private Response.ErrorListener mDefaultErrorListener;

    private HttpClient mClient;

    public static NetworkManager getInstance(Context context){
        if(mManager == null){
            mManager = new NetworkManager(context);
        }
        return mManager;
    }

    private NetworkManager(final Context context){
        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
        mDefaultErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.isNeedShow()){
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }

    public ImageLoader getImageLoader(){
        return mImageLoader;
    }

    public void getResultClass(String url, Class objectClass, Response.Listener listener){
        this.getResultClass(url, null, objectClass, listener, mDefaultErrorListener);
    }

    public void getResultClass(String url, Class objectClass, Response.Listener listener,
                               Response.ErrorListener errorListener){
        this.getResultClass(url, null, objectClass, listener, errorListener);
    }

    public void getResultClass(String url, Map<String, String> params, Class objectClass,
                               Response.Listener listener){
        this.getResultClass(url, params, objectClass, listener, mDefaultErrorListener);
    }

    public void getResultClass(String url, Map<String, String> params, Class objectClass,
                               Response.Listener listener, Response.ErrorListener errorListener){
        GsonRequest request = new GsonRequest(Request.Method.GET, url + "?" + addParams(params),
                null, objectClass, listener, errorListener);
        mRequestQueue.add(request);
    }

    public void getPostResultClass(String url, Map<String, String> params, Class objectClass,
                                   Response.Listener listener, Response.ErrorListener errorListener){
        GsonRequest request = new GsonRequest(Request.Method.POST, url, params, objectClass, listener,
                errorListener == null ? mDefaultErrorListener : errorListener);
        mRequestQueue.add(request);
    }

    public String uploadImage(String url, Map<String, String> params){
        if(params == null){
            return null;
        }
        if(mClient == null){
            mClient = new DefaultHttpClient();
        }
        String result = null;
        try {
            HttpPost request = new HttpPost(url);
            MultipartEntity entity = new MultipartEntity();
            for(String key : params.keySet()){
                if("file".equals(key)){
                    entity.addPart(key, new FileBody(new File(params.get(key))));
                }
                entity.addPart(key, new StringBody(params.get(key)));
            }
            request.setEntity(entity);
            HttpResponse response = mClient.execute(request);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void getResultString(String url, Response.Listener<String> mListener){
        this.getResultString(url, null, mListener);
    }

    public void getResultString(String url, Map<String, String> params, Response.Listener<String> mListener){
        this.getResultString(url, params, mListener, null);
    }

    public void getResultString(String url, Map<String, String> params,
                                Response.Listener<String> mListener, Response.ErrorListener errorListener){
        String finalUrl = url + "?" + addParams(params);
        StringRequest request = new StringParamsRequest(Request.Method.GET,
                finalUrl,null, mListener, errorListener == null ? mDefaultErrorListener : errorListener);
        mRequestQueue.add(request);
    }

    public void getPostResultString(String url, Response.Listener<String> mListener){
        this.getPostResultString(url, null, mListener);
    }

    public void getPostResultString(String url, Map<String, String> params, Response.Listener<String> mListener){
        this.getPostResultString(url, params, mListener, mDefaultErrorListener);
    }

    public void getPostResultString(String url, Map<String, String> params, Response.Listener<String> mListener, Response.ErrorListener errorListener){
    	StringRequest request = new StringParamsRequest(Request.Method.POST,url, params, mListener, errorListener);
        mRequestQueue.add(request);
    }
    
    private static String addParams(Map<String, String> params) {
        StringBuilder builder = new StringBuilder();
        if(params != null){
            for(Map.Entry<String, String> item : params.entrySet()){
                builder.append(item.getKey()).append("=").append(item.getValue()).append("&");
            }
        }
        builder.append("timestamp_now=").append(System.currentTimeMillis());
//        if(!TextUtils.isEmpty(Application.userToken)){
//            builder.append("&user_token=").append(Application.userToken);
//        }
        return builder.toString();
    }
}
