package com.hello1987.restful.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;

import com.hello1987.restful.processor.Processor;
import com.hello1987.restful.processor.ProcessorCallback;
import com.hello1987.restful.processor.ProcessorFactory;
import com.hello1987.restful.rest.resource.Resource;

import java.util.HashMap;

/**
 * Created by yangningbo on 2016/3/24.
 */
public class RESTfulService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onStart(intent, startId);
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        final int resourceType = intent.getIntExtra(ServiceContract.RESOURCE_TYPE, -1);

        final HashMap<String, Object> requestParams = (HashMap<String, Object>) intent.getSerializableExtra(ServiceContract.REQUEST_PARAMS);
        final String extraKey = (String) requestParams.get(ServiceContract.EXTRA_KEY);
        final ResultReceiver serviceCallback = intent.getParcelableExtra(ServiceContract.SERVICE_CALLBACK);

        Processor processor = ProcessorFactory.newInstance(this).getProcessor(resourceType);
        if (processor == null) {
            if (serviceCallback != null) {
                serviceCallback.send(ServiceContract.REQUEST_INVALID, buildResultData(intent));
            }

            stopSelf(startId);
            return;
        }

        processor.process(requestParams, buildProcessorCallback(extraKey, intent, serviceCallback, startId));
    }

    private Bundle buildResultData(Intent intent) {
        Bundle resultData = new Bundle();
        resultData.putParcelable(ServiceContract.ORIGINAL_INTENT, intent);
        return resultData;
    }

    private ProcessorCallback buildProcessorCallback(final String extraKey, final Intent intent, final ResultReceiver serviceCallback, final int startId) {
        return new ProcessorCallback() {
            @Override
            public void send(int resultCode, Resource resource) {
                if (serviceCallback != null) {
                    intent.putExtra(ServiceContract.EXTRA_KEY, extraKey);

                    // 添加返回结果
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ServiceContract.RESOURCE_DATA, resource);
                    intent.putExtras(bundle);

                    serviceCallback.send(ServiceContract.REQUEST_INVALID, buildResultData(intent));
                }

                stopSelf(startId);
            }
        };
    }

}
