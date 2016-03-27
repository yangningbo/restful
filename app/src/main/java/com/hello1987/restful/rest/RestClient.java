package com.hello1987.restful.rest;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by yangningbo on 2016/3/25.
 */
public class RestClient {

    private static final String TAG = RestClient.class.getSimpleName();

    private static RestClient sInstance;
    private Context mContext;
    private RequestQueue mRequestQueue;

    private RestClient(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static RestClient getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new RestClient(context);
        }
        return sInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request, Object tag) {
        request.setTag(tag == null ? TAG : tag);
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object tag) {
        getRequestQueue().cancelAll(tag == null ? TAG : tag);
    }

}
