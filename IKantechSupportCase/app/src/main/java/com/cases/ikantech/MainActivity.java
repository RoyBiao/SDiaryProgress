package com.cases.ikantech;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.cases.ikantech.base.CustomTitleActivity;
import com.cases.ikantech.service.CsService;
import com.cases.ikantech.ui.FrescoActivity;
import com.cases.ikantech.ui.OkHttpActivity;
import com.cases.ikantech.ui.OtherActivity;
import com.ikantech.support.task.YiRunnable;
import com.ikantech.support.task.YiTask;
import com.ikantech.support.task.listener.YiTaskListener;

import java.io.IOException;
import java.util.LinkedHashSet;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends CustomTitleActivity {
    Runnable mRunnable1 = null;
    Runnable mRunnable2 = null;
    YiTask[] task = new YiTask[5];
    Call call = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected void installListeners() {
        installLocalServiceBinder();
    }

    @Override
    protected void uninstallListeners() {
        uninstallLocalServiceBinder();
    }


    @Override
    public void processHandlerMessage(Message msg) {

    }

    public void showClick(View view) {
        switch (view.getId()) {
            case R.id.showDialogBt:  //Dialog代理
                showMsgDialog("hello world", "ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showToast("Hello world");
                    }
                });
                break;
            case R.id.showToastBt: //Toast代理
                showProgressDialog();
                showToast("Hello world");
                break;
            case R.id.localSingleThread: //线程队列
                if (mRunnable1 == null) {
                    mRunnable1 = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                //getLocalService().removeExecute(mRunnable2);
                                showMsgDialog("Hello");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    getLocalService().execute(mRunnable1);
                }

                if (mRunnable2 == null) {
                    mRunnable2 = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                cancelMsgDialog();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    getLocalService().execute(mRunnable2);
                }
                break;
            case R.id.localService:  //Ibinder代理
                CsService.CsBinder s = (CsService.CsBinder) getLocalIBinder();
                s.startRunnable();
                startActivity(new Intent(MainActivity.this, OtherActivity.class));
                break;

            case R.id.AsyExecute:   //线程池
                for (int i = 0; i < 5; i++) {
                    task[i] = new YiTask();
                    YiRunnable runnable = new YiRunnable();
                    runnable.setListener(new YiTaskListener() {
                        @Override
                        public void get() {
                            super.get();
                            for (int i = 0; i < 300; i++) {
                                System.out.println("aaa:" + Thread.currentThread().getName() + i);
                            }
                        }


                        @Override
                        public void update() {
                            super.update();
                            System.out.println("2-2-2onPostExecute");
                        }

                    });
                    task[i].execute(runnable);
                }

                break;

            case R.id.AsyCancel:  //终止线程池
                LinkedHashSet<YiTask> set = YiTask.getTaskSet();
                for (YiTask task1 : set)
                    task1.cancel(true);
//                for (int i=0;i<5;i++)
//                    task[i].cancel(true);
                break;

            case R.id.OkHttpGet: //okhttp get
                okhttpGet();
                break;

            case R.id.OkHttpCancel: //okhttp cancel
                call.cancel();
                break;
            case R.id.fresco:
                startActivity(new Intent(MainActivity.this, FrescoActivity.class));
                break;

            case R.id.okhttp:
                startActivity(new Intent(MainActivity.this, OkHttpActivity.class));

                break;
        }
    }

    public void okhttpGet() {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url("http://baidu.com/")
                .build();
        //new call
        call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                System.out.println("onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("response:" + response.body().string());
            }
        });
    }
}
