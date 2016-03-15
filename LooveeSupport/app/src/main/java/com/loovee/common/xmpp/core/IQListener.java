package com.loovee.common.xmpp.core;

import android.os.Handler;
import android.os.Looper;

import com.loovee.common.bean.BaseIQResults;
import com.loovee.common.xmpp.packet.DefaultIQ;
import com.loovee.common.xmpp.packet.Packet;

/**
 * Created by Administrator on 2016/3/1 0001.
 */
public abstract class IQListener<T extends BaseIQResults> implements PacketListener {
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void processPacket(final Packet packet) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final Object result = ((DefaultIQ<Object>) packet).getData();
                processIQ((T) result);
            }
        });
    }


    public abstract void processIQ(T t);
}
