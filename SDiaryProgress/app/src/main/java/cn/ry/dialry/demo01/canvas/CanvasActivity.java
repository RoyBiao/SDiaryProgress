package cn.ry.dialry.demo01.canvas;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
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

        MyView myView = (MyView) findViewById(R.id.myView);
        myView.loadData(new Point(50, 200), "desc", "brand", "price");

        LinearLayout layout = (LinearLayout) findViewById(R.id.llayout);
        MyView myView2 = new MyView(this);
        myView2.loadData(new Point(700, 1200), "desc", "brand", "price");
        layout.addView(myView2);
    }
}
