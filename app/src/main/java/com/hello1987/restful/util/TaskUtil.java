package com.hello1987.restful.util;

import android.os.AsyncTask;
import android.os.Build;

/**
 * Created by yangningbo on 2016/3/25.
 */
public class TaskUtil {

    private TaskUtil() {
    }

    public static <Params, Progress, Result> void executeAsyncTask(AsyncTask<Params, Progress, Result> task, Params... params) {
        if (Build.VERSION.SDK_INT >= 11) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }

}
