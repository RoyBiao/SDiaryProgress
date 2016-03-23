package com.cases.ikantech.ui;

import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.cases.ikantech.R;
import com.cases.ikantech.base.CustomTitleActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by ruibiao on 16-3-23.
 */
public class FrescoActivity extends CustomTitleActivity implements View.OnClickListener {
    private SimpleDraweeView mBaselineJpegView;
    private Button mFrescoGifBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_fresco);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        mFrescoGifBtn = (Button) findViewById(R.id.frescoGif);
        mBaselineJpegView = (SimpleDraweeView) findViewById(R.id.baseline_jpeg);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected void installListeners() {
        mFrescoGifBtn.setOnClickListener(this);
    }

    @Override
    protected void uninstallListeners() {

    }

    @Override
    public void processHandlerMessage(Message msg) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frescoProgress:
                Uri uri = Uri.parse("http://g.hiphotos.baidu.com/image/pic/item/6c224f4a20a446230761b9b79c22720e0df3d7bf.jpg");
//                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
//                        .setProgressiveRenderingEnabled(true)
//                        .build();
//                DraweeController controller = Fresco.newDraweeControllerBuilder()
//                        .setImageRequest(request)
//                        .build();
//                mBaselineJpegView.setController(controller);
                mBaselineJpegView.setImageURI(uri);
                break;
            case R.id.frescoGif:
                DraweeController animatedGifController = Fresco.newDraweeControllerBuilder()
                        .setAutoPlayAnimations(true)
                        .setUri(Uri.parse("https://s3.amazonaws.com/giphygifs/media/4aBQ9oNjgEQ2k/giphy.gif"))
                        .build();
                mBaselineJpegView.setController(animatedGifController);
                break;
        }
    }
}
