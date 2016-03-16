package com.loovee.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.loovee.common.app.LooveeApplication;
import com.loovee.common.util.LogUtils;
import com.loovee.common.xmpp.core.UserAuthentication;
import com.loovee.common.xmpp.core.XMPPConnection;
import com.loovee.common.xmpp.core.XMPPException;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void mainClick(View view){
        switch (view.getId()){
            case R.id.login:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final XMPPConnection connection = LooveeApplication.instances.getXMPPConnection();
                        try {
                            if (connection.isConnected()) {
                                connection.disconnect();
                            }
                            connection.connect();
                            if (connection.isConnected()) {
                                LogUtils.jLog().i("1111111116");
                            }
                            connection.login("8613824125203", "123456", UserAuthentication.LoginType.tel);
                        } catch (XMPPException e) {
                            e.printStackTrace();
                        } finally {
                        }
                    }
                }).start();
                break;

            case R.id.register:
                break;
        }
    }
}
