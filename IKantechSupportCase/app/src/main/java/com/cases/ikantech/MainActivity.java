package com.cases.ikantech;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.cases.ikantech.base.CustomTitleActivity;
import com.cases.ikantech.service.CsService;

import java.util.ArrayList;

public class MainActivity extends CustomTitleActivity {
    Runnable mRunnable1 = null;
    Runnable mRunnable2 = null;

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
            case R.id.showDialogBt:
//                showMsgDialog();

//                showMsgDialog("hello world", "ok", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showToast("Hello world");
//                    }
//                });
                showProgressDialog();
                break;
            case R.id.showToastBt:
                showToast("Hello world");
                break;
            case R.id.localSingleThread:
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
            case R.id.localService:
                break;
        }
    }
}
