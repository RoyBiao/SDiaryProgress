package com.loovee.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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
        Button button = (Button) this.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.jLog().i("1111111111");
                        final XMPPConnection connection = LooveeApplication.instances.getXMPPConnection();
                        try {
                            LogUtils.jLog().i("1111111112");
                            if (connection.isConnected()) {
                                LogUtils.jLog().i("1111111113");
                                connection.disconnect();
                            }
                            LogUtils.jLog().i("1111111114");
                            connection.connect();
                            LogUtils.jLog().i("1111111115");
                            connection.login("13824125203", "123456", UserAuthentication.LoginType.tel);
                            LogUtils.jLog().i("1111111116");
                        } catch (XMPPException e) {
                            e.printStackTrace();
                        } finally {
                        }
                    }
                }).start();
            }
        });
    }
}
