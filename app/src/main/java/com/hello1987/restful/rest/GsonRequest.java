package com.hello1987.restful.rest;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hello1987.restful.rest.result.Result;
import com.hello1987.restful.util.LogUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class GsonRequest<T extends Result> extends Request<T> {

    private final Gson mGson = new Gson();
    private final Map<String, String> mParams;
    private final Class<T> mClazz;
    private final Listener<Result> mListener;
    private String mToken;

    private String mUrl;

    public GsonRequest(int method, String url, Map<String, Object> params, Class<T> clazz, Listener<Result> listener, ErrorListener errorListener) {
        super(method, method == Method.GET ? getUrl(url, params) : url, errorListener);
        // 设置请求重连次数和超时时间
        setRetryPolicy(new DefaultRetryPolicy(30000, 0, 0));
        if (params.containsKey("token")) {
            mToken = (String) params.get("token");
            params.remove("token");
        }

        mUrl = method == Method.GET ? getUrl(url, params) : url;
        LogUtil.i("[" + getMethod(method) + " " + mUrl + "] " + (params == null ? "{}" : params.toString()));
        mParams = convert(params);
        mClazz = clazz;
        mListener = listener;
    }

    private static String getUrl(String url, Map<String, Object> params) {
        if (url == null) {
            return null;
        }

        if (params != null) {
            StringBuffer sb = new StringBuffer();

            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key == null || "token".equals(key) || value == null) {
                    continue;
                }

                sb.append(sb.length() == 0 ? "?" : "&").append(key).append("=")
                        .append(encodeParam(value.toString()));
            }

            url += sb.toString();
        }

        return url;
    }

    private static String encodeParam(String param) {
        String encodeUrl = null;
        try {
            encodeUrl = URLEncoder.encode(param, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeUrl == null ? param : encodeUrl;
    }

    private String getMethod(int method) {
        switch (method) {
            case Method.GET:
                return "GET";
            case Method.POST:
                return "POST";
            case Method.PUT:
                return "PUT";
            case Method.DELETE:
                return "DELETE";
        }
        return String.valueOf(method);
    }

    private Map<String, String> convert(Map<String, Object> params) {
        Map<String, String> map = new HashMap<String, String>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            Object value = entry.getValue();
            if (value == null) {
                continue;
            }
            map.put(entry.getKey(), value.toString());
        }
        return map;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (!TextUtils.isEmpty(mToken)) {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Authorization", "Token " + mToken);
            return headers;
        }
        return super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (mParams != null && !mParams.isEmpty()) {
            return mParams;
        }
        return super.getParams();
    }

    @Override
    public void deliverResponse(Result result) {
        if (mListener != null) {
            mListener.onResponse(result);
        }
    }

    @Override
    public Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            LogUtil.i("[" + mUrl + "] " + json);
            return Response.success(mGson.fromJson(json, mClazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

}