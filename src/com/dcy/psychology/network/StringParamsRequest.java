package com.dcy.psychology.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.dcy.psychology.util.Constants;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

/**
 * Created by dcy123 on 2015/3/18.
 */
public class StringParamsRequest extends StringRequest{
    private Map<String, String> mParams;

    public StringParamsRequest(int method, String url, Map<String, String> params,
                    Response.Listener listener, Response.ErrorListener errorListener){
        super(method, url, listener, errorListener);
        mParams = params;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
//            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        	parsed = new String(response.data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        try {
            JSONObject resultObject = new JSONObject(parsed);
            if(Constants.Api_Success.equals(resultObject.getString("status"))){
                return Response.success(resultObject.getString("result"), HttpHeaderParser.parseCacheHeaders(response));
            } else {
                return Response.error(new VolleyError(resultObject.getString("result"), resultObject.getInt("code")));
            }
        } catch (Exception e) {
            return Response.error(new VolleyError(e));
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
}
