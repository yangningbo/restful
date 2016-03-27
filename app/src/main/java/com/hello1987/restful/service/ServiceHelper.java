package com.hello1987.restful.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.content.LocalBroadcastManager;

import com.hello1987.restful.processor.ResourceType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by yangningbo on 2016/3/24.
 */
public class ServiceHelper {

    private static final Object LOCK = new Object();
    private static ServiceHelper sInstance;

    private Context mContext;

    private Map<String, Long> mPendingRequests = new HashMap<String, Long>();

    private ServiceHelper(Context context) {
        mContext = context.getApplicationContext();
    }

    public static ServiceHelper getInstance(Context context) {
        synchronized (LOCK) {
            if (sInstance == null) {
                sInstance = new ServiceHelper(context);
            }
        }

        return sInstance;
    }

    private long generateRequestID() {
        return UUID.randomUUID().getLeastSignificantBits();
    }

    public boolean isRequestPending(long requestId) {
        return mPendingRequests.containsValue(requestId);
    }

    public long submit(String requestKey, int resourceType, HashMap<String, Object> requestParams) {
        long requestId = generateRequestID();
        mPendingRequests.put(requestKey, requestId);

        Intent intent = buildIntent(requestId, requestKey, resourceType, requestParams, buildServiceCallback());
        mContext.startService(intent);

        return requestId;
    }

    private ResultReceiver buildServiceCallback() {
        return new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleResponse(resultCode, resultData);
            }
        };
    }

    private Intent buildIntent(long requestId, String requestKey, int resourceType, HashMap<String, Object> requestParams, ResultReceiver serviceCallback) {
        Intent intent = new Intent(mContext, RESTfulService.class);

        intent.putExtra(ServiceContract.REQUEST_ID, requestId);
        intent.putExtra(ServiceContract.REQUEST_KEY, requestKey);
        intent.putExtra(ServiceContract.RESOURCE_TYPE, resourceType);

        // 添加请求参数
        Bundle bundle = new Bundle();
        bundle.putSerializable(ServiceContract.REQUEST_PARAMS, requestParams);
        intent.putExtras(bundle);

        intent.putExtra(ServiceContract.SERVICE_CALLBACK, serviceCallback);

        return intent;
    }

    private void handleResponse(int resultCode, Bundle resultData) {
        Intent origIntent = resultData.getParcelable(ServiceContract.ORIGINAL_INTENT);

        if (origIntent != null) {
            String requestKey = origIntent.getStringExtra(ServiceContract.REQUEST_KEY);
            mPendingRequests.remove(requestKey);

            origIntent.setAction(ServiceContract.ACTION_RESULT);
            origIntent.putExtra(ServiceContract.RESULT_CODE, resultCode);
        } else {
            origIntent = new Intent(ServiceContract.ACTION_RESULT);
            origIntent.putExtra(ServiceContract.RESULT_CODE, resultCode);
        }

        // LocalBroadcastManager防止越界
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(origIntent);
    }

    public long createMessage(HashMap<String, Object> requestParams) {
        return submit(RequestKey.CREATE_MESSAGE, ResourceType.CREATE_MESSAGE, requestParams);
    }

    public long getMessage(HashMap<String, Object> requestParams) {
        return submit(RequestKey.GET_MESSAGE, ResourceType.GET_MESSAGE, requestParams);
    }

    public long getMessages(HashMap<String, Object> requestParams) {
        return submit(RequestKey.GET_MESSAGES, ResourceType.GET_MESSAGES, requestParams);
    }

    public long updateMessage(HashMap<String, Object> requestParams) {
        return submit(RequestKey.UPDATE_MESSAGE, ResourceType.UPDATE_MESSAGE, requestParams);
    }

    public long deleteMessage(HashMap<String, Object> requestParams) {
        return submit(RequestKey.DELETE_MESSAGE, ResourceType.DELETE_MESSAGE, requestParams);
    }

}
