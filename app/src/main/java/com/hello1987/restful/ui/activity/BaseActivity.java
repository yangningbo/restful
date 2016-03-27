package com.hello1987.restful.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.hello1987.restful.processor.ResultCode;
import com.hello1987.restful.rest.resource.Resource;
import com.hello1987.restful.service.ServiceContract;

import java.util.HashMap;

/**
 * Created by yangningbo on 2016/3/27.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private BroadcastReceiver mRequestReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        setContentView(getLayoutId());

        initView();

        initData();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();


    @Override
    protected void onResume() {
        super.onResume();

        // registerReceiver
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(ServiceContract.ACTION_RESULT);
        mRequestReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long requestId = intent.getLongExtra(ServiceContract.REQUEST_ID, -1);
                String extraKey = intent.getStringExtra(ServiceContract.EXTRA_KEY);

                int resultCode = intent.getIntExtra(ServiceContract.RESULT_CODE, -1);
                HashMap<String, Object> requestParams = (HashMap<String, Object>) intent.getSerializableExtra(ServiceContract.REQUEST_PARAMS);
                Resource resource = (Resource) intent.getSerializableExtra(ServiceContract.RESOURCE_DATA);

                if (resultCode == ResultCode.NO_ERROR) {
                    onSuccess(requestId, extraKey, requestParams, resultCode, resource, intent);
                } else {
                    onFailure(requestId, extraKey, requestParams, resultCode, intent);
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mRequestReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // unregisterReceiver
        unregisterReceiver();
    }

    private void unregisterReceiver() {
        if (mRequestReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRequestReceiver);
            mRequestReceiver = null;
        }
    }

    protected void onSuccess(long requestId, String extraKey, HashMap<String, Object> requestParams, int resultCode, Resource resource, Intent intent) {
    }

    protected void onFailure(long requestId, String extraKey, HashMap<String, Object> requestParams, int resultCode, Intent intent) {
    }


}
