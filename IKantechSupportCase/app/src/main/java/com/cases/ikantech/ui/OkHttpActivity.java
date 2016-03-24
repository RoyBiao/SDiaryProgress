package com.cases.ikantech.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.cases.ikantech.R;
import com.cases.ikantech.base.CustomTitleActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ruibiao on 16-3-24.
 */
public class OkHttpActivity extends CustomTitleActivity {
    Call call = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_okhttp);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        okhttpGet();
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected void installListeners() {

    }

    @Override
    protected void uninstallListeners() {

    }

    @Override
    public void processHandlerMessage(Message msg) {

    }

    @Override
    public void onTitleBarLeftBtnClick(View view) {
        call.cancel();
        super.onTitleBarLeftBtnClick(view);
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
                System.out.println("OkHttpActivity111 onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("OkHttpActivity111 response:" + response.body().string());
                Handler handler =new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(OkHttpActivity.this);
                        builder.setMessage("Are you sure you want to exit?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        OkHttpActivity.this.finish();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }).show();
                    }
                },2000);
                System.out.println("OkHttpActivity111 onSuccess");
            }
        });
    }
}
