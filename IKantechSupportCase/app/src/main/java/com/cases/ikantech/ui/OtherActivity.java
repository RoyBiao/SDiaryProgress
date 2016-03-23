package com.cases.ikantech.ui;

import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.cases.ikantech.R;
import com.cases.ikantech.base.CustomTitleActivity;
import com.cases.ikantech.service.CsService;

/**
 * Created by ruibiao on 16-3-22.
 */
public class OtherActivity extends CustomTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_other);
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

    public void otherClick(View view){
        switch (view.getId()){
            case R.id.stopRunnable:
                CsService.CsBinder binder = (CsService.CsBinder) getLocalIBinder();
                binder.stopRunable();
                break;
        }
    }
}
