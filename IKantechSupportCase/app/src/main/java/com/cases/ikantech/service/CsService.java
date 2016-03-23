package com.cases.ikantech.service;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.ikantech.support.service.YiLocalService;

/**
 * Created by ruibiao on 16-2-23.
 */
public class CsService extends YiLocalService {
    public CsBinder binder = new CsBinder(this);
    AsyncTask mAsy;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class CsBinder extends YiLocalServiceBinder {
        public CsBinder(YiLocalService s) {
            super(s);
        }

        public void startRunnable() {
            mAsy=new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    for (int i = 0; i < 300000; i++) {
                        System.out.println("aaa" + i);
                    }
                    return null;
                }

                @Override
                protected void onCancelled() {
                    super.onCancelled();
                    System.out.println("onCancelled");
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    System.out.println("onPostExecute");
                }
            };
            mAsy.execute();
        }

        public void stopRunable() {
            mAsy.cancel(true);
        }

    }
}
