package com.hello1987.restful.processor;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.android.volley.Request;
import com.hello1987.restful.provider.MessageContract;
import com.hello1987.restful.rest.API;
import com.hello1987.restful.rest.GsonRequest;
import com.hello1987.restful.rest.resource.Message;
import com.hello1987.restful.rest.result.MessageResult;
import com.hello1987.restful.rest.result.Result;

import java.util.Map;

/**
 * Created by yangningbo on 2016/3/25.
 */
public class CreateMessageProcessor extends AbstractProcessor {

    private Context mContext;

    public CreateMessageProcessor(Context context) {
        super(context);
    }

    @Override
    public void process(Map<String, Object> params, ProcessorCallback callback) {

        // (4) Insert results column with status column

        // (5) Call REST client
        executeRequest(new GsonRequest<MessageResult>(Request.Method.POST, API.CREATE_MESSAGE, params, MessageResult.class, buildResponseListener(params, callback), buildErrorListener(params, callback)));

    }

    @Override
    protected void onResult(Map<String, Object> params, int code, Result result, ProcessorCallback callback) {
        Message message = null;
        if (code == ResultCode.NO_ERROR) {
            if (result != null) {
                message = ((MessageResult) result).getResult();

                // (8) Update status column
            }
        } else {
            // (8) Update status column
        }

        // (9) Callback to Service
        if (callback != null) {
            callback.send(code, message);
        }
    }

    private void updateMessage(Message message) {
        if (message != null) {
            ContentValues values = new ContentValues();
            values.put(MessageContract.MessageTable.CREATED, message.getCreated());

            Cursor cursor = mContext.getContentResolver().query(MessageContract.MessageTable.CONTENT_URI, null, null, null, null);
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MessageContract.MessageTable._ID));
                mContext.getContentResolver().update(
                        ContentUris.withAppendedId(MessageContract.MessageTable.CONTENT_URI, id), values,
                        null, null);
            } else {
                mContext.getContentResolver().insert(MessageContract.MessageTable.CONTENT_URI, values);
            }
            cursor.close();
        }
    }

}
