package com.ikantech.support.task;

import android.os.AsyncTask;

import com.ikantech.support.task.listener.YiTaskListListener;
import com.ikantech.support.task.listener.YiTaskListener;
import com.ikantech.support.task.listener.YiTaskObjectListener;
import com.ikantech.support.util.YiLog;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * 线程池
 */
public class YiTask extends AsyncTask<YiRunnable, Integer, YiRunnable> {
    /**
     * 收集需要cancel的异步线程
     */
    private final static LinkedHashSet<YiTask> mTaskSet = new LinkedHashSet<YiTask>();

    /**
     * listener.
     */
    private YiTaskListener listener;

    /**
     * The result.
     */
    private Object result;

    /**
     * Instantiates a new ab task.
     */
    public YiTask() {
        super();
        mTaskSet.add(this);
    }

    /**
     * Instantiates a new ab task.
     *
     * @param isSet true 添加到mTaskSet
     */
    public YiTask(boolean isSet) {
        super();
        if (isSet) {
            mTaskSet.add(this);
        }
    }

    /**
     * Instantiates a new ab task.
     */
    public static YiTask newInstance() {
        YiTask mAbTask = new YiTask();
        return mAbTask;
    }

    /**
     * Instantiates a new ab task.
     *
     * @param isSet true 添加到mTaskSet
     */
    public static YiTask newInstance(boolean isSet) {
        YiTask mAbTask = new YiTask(isSet);
        return mAbTask;
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected YiRunnable doInBackground(YiRunnable... items) {
        YiRunnable item = items[0];
        this.listener = item.getListener();
        if (this.listener != null) {
            if (this.listener instanceof YiTaskListListener) {
                result = ((YiTaskListListener) this.listener).getList();
            } else if (this.listener instanceof YiTaskObjectListener) {
                result = ((YiTaskObjectListener) this.listener).getObject();
            } else {
                this.listener.get();
            }
        }
        return item;
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#onCancelled()
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();
        YiLog.getInstance().i("onCancelled");
        mTaskSet.remove(this);
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(YiRunnable item) {
        mTaskSet.remove(this);
        if (this.listener != null) {
            if (this.listener instanceof YiTaskListListener) {
                ((YiTaskListListener) this.listener).update((List<?>) result);
            } else if (this.listener instanceof YiTaskObjectListener) {
                ((YiTaskObjectListener) this.listener).update(result);
            } else {
                this.listener.update();
            }
        }
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#onPreExecute()
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#onProgressUpdate(Progress[])
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (this.listener != null) {
            this.listener.onProgressUpdate(values);
        }
    }

    public static LinkedHashSet<YiTask> getTaskSet() {
        return mTaskSet;
    }
}
