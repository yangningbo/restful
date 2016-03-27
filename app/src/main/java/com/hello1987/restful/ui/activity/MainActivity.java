package com.hello1987.restful.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.hello1987.restful.R;
import com.hello1987.restful.provider.MessageContract;
import com.hello1987.restful.rest.resource.Message;
import com.hello1987.restful.rest.resource.Resource;
import com.hello1987.restful.service.ServiceHelper;
import com.hello1987.restful.util.LogUtil;
import com.hello1987.restful.util.TaskUtil;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ListView mListView;
    private Button mGetMessagesButton;
    private Button mCreateMessageButton;

    private ServiceHelper mServiceHelper;

    private long mGetMessagesRequestId;
    private long mCreateMessageRequestId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mListView = (ListView) findViewById(R.id.list_view);
        mGetMessagesButton = (Button) findViewById(R.id.get_messages_btn);
        mCreateMessageButton = (Button) findViewById(R.id.create_message_btn);

        mGetMessagesButton.setOnClickListener(this);
        mCreateMessageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_messages_btn:
                getMessages();
                break;
            case R.id.create_message_btn:
                createMessage();
                break;
            default:
                break;
        }
    }


    private void createMessage() {
        mCreateMessageRequestId = mServiceHelper.createMessage(null);
    }

    @Override
    protected void initData() {
        mServiceHelper = ServiceHelper.getInstance(this);

        // a.使用ContentObserver
//        getContentResolver().registerContentObserver(Uri, true, ContentObserver);

        // b.使用LoaderManager, 异步查询
//        getLoaderManager().initLoader(id, Bundle, LoaderCallbacks);

        // c.直接使用ServiceHelper Callback
        getMessages();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getMessagesFromDB();
    }

    private void getMessages() {
        mGetMessagesRequestId = mServiceHelper.getMessages(null);
    }

    private void getMessagesFromDB() {
        TaskUtil.executeAsyncTask(new AsyncTask<Void, Void, List<Message>>() {
            @Override
            protected List<Message> doInBackground(Void... params) {
                List<Message> messages = null;

                Cursor cursor = getContentResolver().query(MessageContract.MessageTable.CONTENT_URI, null, null, null, null);

                while (cursor.moveToNext()) {
                    //TODO

                    Message message = new Message();
                    messages.add(message);
                }

                cursor.close();

                return messages;
            }

            @Override
            protected void onPostExecute(List<Message> messages) {
                super.onPostExecute(messages);
                //TODO
            }
        });
    }

    @Override
    protected void onSuccess(long requestId, String extraKey, HashMap<String, Object> requestParams, int resultCode, Resource resource, Intent intent) {
        super.onSuccess(requestId, extraKey, requestParams, resultCode, resource, intent);

        LogUtil.i(TAG, "onSuccess intent " + intent.getAction() + ", request ID " + requestId);

        if (requestId == mGetMessagesRequestId) {

            LogUtil.i(TAG, "Result is for our request ID");

            LogUtil.i(TAG, "Extra Key=" + extraKey);

            LogUtil.i(TAG, "Request params = " + requestParams.toString());

            LogUtil.i(TAG, "Result code = " + resultCode);

            LogUtil.i(TAG, resource != null ? "Resource != null" : "Resource == null");

            // TODO
        } else if (requestId == mCreateMessageRequestId) {
            //TODO
        } else {
            LogUtil.i(TAG, "Result is NOT for our request ID");
        }
    }

    @Override
    protected void onFailure(long requestId, String extraKey, HashMap<String, Object> requestParams, int resultCode, Intent intent) {
        super.onFailure(requestId, extraKey, requestParams, resultCode, intent);

        LogUtil.i(TAG, "onFailure intent " + intent.getAction() + ", request ID " + requestId);

        if (requestId == mGetMessagesRequestId) {

            LogUtil.i(TAG, "Result is for our request ID");

            LogUtil.i(TAG, "Extra Key=" + extraKey);

            LogUtil.i(TAG, "Request params = " + requestParams.toString());

            LogUtil.i(TAG, "Result code = " + resultCode);

            //TODO
        } else if (requestId == mCreateMessageRequestId) {
            //TODO
        } else {
            LogUtil.i(TAG, "Result is NOT for our request ID");
        }
    }

}
