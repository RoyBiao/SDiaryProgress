package com.cases.ikantech.app;

import android.content.Intent;
import android.graphics.Bitmap;

import com.cases.ikantech.service.CsService;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.ikantech.support.app.YiApplication;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ruibiao on 16-2-23.
 */
public class CsApplication extends YiApplication {

    @Override
    protected void initialize() {
        super.initialize();
        initFresco();
        startService(new Intent(this,CsService.class));
    }

    private void initFresco(){
        FLog.setMinimumLoggingLevel(FLog.VERBOSE);
        Set<RequestListener> listeners = new HashSet<>();
        listeners.add(new RequestLoggingListener());
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setRequestListeners(listeners)
                .setBitmapsConfig(Bitmap.Config.ARGB_8888)
                .build();
        Fresco.initialize(this, config);
    }
}
