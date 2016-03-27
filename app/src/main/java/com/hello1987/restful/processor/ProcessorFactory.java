package com.hello1987.restful.processor;

import android.content.Context;

public class ProcessorFactory {

    private static final Object LOCK = new Object();
    private static ProcessorFactory sInstance;

    private Context mContext;

    private ProcessorFactory(Context context) {
        mContext = context.getApplicationContext();
    }

    public static ProcessorFactory newInstance(Context context) {
        synchronized (LOCK) {
            if (sInstance == null) {
                sInstance = new ProcessorFactory(context);
            }
        }

        return sInstance;
    }

    public Processor getProcessor(int resourceType) {
        switch (resourceType) {
            case ResourceType.CREATE_MESSAGE:
                return new CreateMessageProcessor(mContext);
            case ResourceType.GET_MESSAGE:
                return new GetMessageProcessor(mContext);
            case ResourceType.GET_MESSAGES:
                return new GetMessagesProcessor(mContext);
            case ResourceType.UPDATE_MESSAGE:
                return new UpdateMessageProcessor(mContext);
            case ResourceType.DELETE_MESSAGE:
                return new DeleteMessageProcessor(mContext);
            default:
                return null;
        }
    }

}
