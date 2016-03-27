package com.hello1987.restful.processor;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.hello1987.restful.rest.RestClient;
import com.hello1987.restful.rest.result.Result;

import java.util.Map;

/**
 * Created by yangningbo on 2016/3/25.
 */
public abstract class AbstractProcessor implements Processor {

    private Context mContext;

    public AbstractProcessor(Context context) {
        mContext = context;
    }

    protected <T> void executeRequest(Request<T> request) {
        RestClient.getInstance(mContext).addToRequestQueue(request);
    }

    protected <T extends Result> Response.Listener<T> buildResponseListener(final Map<String, Object> params, final ProcessorCallback callback) {
        return new Response.Listener<T>() {
            @Override
            public void onResponse(T result) {
                onResult(params, ResultCode.NO_ERROR, result, callback);
            }
        };
    }

    protected Response.ErrorListener buildErrorListener(final Map<String, Object> params, final ProcessorCallback callback) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                } else if (error instanceof TimeoutError) {
                    onResult(params, ResultCode.TIMEOUT_ERROR, null, callback);
                } else if (error instanceof AuthFailureError) {
                    onResult(params, ResultCode.AUTH_ERROR, null, callback);
                } else if (error instanceof ParseError) {
                    onResult(params, ResultCode.PARSE_ERROR, null, callback);
                } else if (error instanceof ServerError) {
                    onResult(params, ResultCode.SERVER_ERROR, null, callback);
                } else {
                    onResult(params, ResultCode.UNKNOWN_ERROR, null, callback);
                }
            }
        };
    }

    protected abstract void onResult(Map<String, Object> params, int code, Result result, ProcessorCallback callback);

}
