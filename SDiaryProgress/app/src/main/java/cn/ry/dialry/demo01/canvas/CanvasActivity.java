package cn.ry.dialry.demo01.canvas;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import cn.ry.dialry.R;

/**
 * Created by ruibiao on 16-4-28.
 */
public class CanvasActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        PicInfoImageView picInfoImageView = new PicInfoImageView(this);
        frameLayout.addView(picInfoImageView);


        LinearLayout layout1 = (LinearLayout) findViewById(R.id.llayout);
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) layout1.getLayoutParams();
        params1.width = YiDeviceUtils.getDisplayMetrics(this).widthPixels;
        params1.height = YiDeviceUtils.getDisplayMetrics(this).widthPixels;
        layout1.setLayoutParams(params1);
        MyView myView1 = new MyView(this);
        myView1.loadData(params1.width, params1.width, new Point(50, 200), "desc", "brand", "price");
        layout1.addView(myView1);

        LinearLayout layout2 = (LinearLayout) findViewById(R.id.llayout2);
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) layout2.getLayoutParams();
        params2.width = YiDeviceUtils.getDisplayMetrics(this).widthPixels;
        params2.height = YiDeviceUtils.getDisplayMetrics(this).widthPixels;
        layout2.setLayoutParams(params2);
        MyView myView2 = new MyView(this);
        myView2.loadData(params2.width, params2.width, new Point(50, 200), "desc", "brand", "price");
        layout2.addView(myView2);

        LinearLayout layout3 = (LinearLayout) findViewById(R.id.llayout3);
        LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) layout3.getLayoutParams();
        params3.width = YiDeviceUtils.getDisplayMetrics(this).widthPixels;
        params3.height = YiDeviceUtils.getDisplayMetrics(this).widthPixels;
        layout3.setLayoutParams(params3);
        MyView myView3 = new MyView(this);
        myView3.loadData(params2.width, params2.width, new Point(50, 200), "desc", "brand", "price");
        layout3.addView(myView3);


        LinearLayout layout4 = (LinearLayout) findViewById(R.id.llayout4);
        layout4.setLayoutParams(params2);
        MyView myView4 = new MyView(this);
        myView4.loadData(params2.width, params2.width, new Point(50, 200), "desc", "brand", "price");
        layout4.addView(myView4);
    }
}
