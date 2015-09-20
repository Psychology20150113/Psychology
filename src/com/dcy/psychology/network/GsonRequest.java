package com.dcy.psychology.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.dcy.psychology.util.Constants;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

/**
 * Created by dcy123 on 2015/3/18.
 */
public class GsonRequest extends Request{
    private Gson mGson;
    private Map<String, String> mParams;
    private Class mClass;
    private Type mType;
    private Response.Listener mListener;
    private Response.ErrorListener mErrorListener;

    public GsonRequest(int method, String url, Map<String, String> params, Class objectClass,
                       Response.Listener listener, Response.ErrorListener errorListener){
        super(method, url, errorListener);
        mListener = listener;
        mErrorListener=errorListener;
        mParams = params;
        mClass = objectClass;
        mGson = new Gson();
    }
    
    public GsonRequest(int method, String url, Map<String, String> params, Type objectType,
            Response.Listener listener, Response.ErrorListener errorListener){
		super(method, url, errorListener);
		mListener = listener;
		mErrorListener=errorListener;
		mParams = params;
		mType = objectType;
		mGson = new Gson();
	}

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            JSONObject resultObject = new JSONObject(jsonString);
            if(Constants.Api_Success.equals(resultObject.getString("status"))){
                return Response.success(mGson.fromJson(resultObject.getString("result"), mClass == null ? mType : mClass),
                        HttpHeaderParser.parseCacheHeaders(response));
            } else {
                return Response.error(new VolleyError(resultObject.getString("result"), resultObject.getInt("code")));
            }
        } catch (Exception exception){
            return Response.error(new ParseError(exception));
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if(mParams == null){
            mParams = new HashMap<String, String>();
        }
        mParams.put("timestamp_now", String.valueOf(System.currentTimeMillis()));
//        if(!TextUtils.isEmpty(Application.userToken)){
//            mParams.put("user_token", Application.userToken);
//        }
        return mParams;
    }

    @Override
    protected void deliverResponse(Object response) {
        mListener.onResponse(response);
    }
}
