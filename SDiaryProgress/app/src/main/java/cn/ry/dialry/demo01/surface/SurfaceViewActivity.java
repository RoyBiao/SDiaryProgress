package cn.ry.dialry.demo01.surface;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import cn.ry.dialry.R;
import cn.ry.dialry.util.YiLog;

/**
 * Created by ruibiao on 16-7-6.
 */
public class SurfaceViewActivity extends FragmentActivity implements GestureDetector.OnGestureListener {
    private GestureDetector detector;
    static int position = 0;
    FragmentManager fragmentManager;
    private DemoSurfaceView demoSurfaceView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_surface_view);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        detector = new GestureDetector(this);
        fragmentManager = getSupportFragmentManager();
        demoSurfaceView1 = (DemoSurfaceView) findViewById(R.id.demoSurfaceView1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        demoSurfaceView1.init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("Fling", "Activity onTouchEvent!");
        return this.detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        YiLog.getInstance().i("Fling Happened!");
        if (e1.getX() - e2.getX() > 120) {
            YiLog.getInstance().i("Fling1");
            if (position < 4) {
                position++;
                YiLog.getInstance().i("Fling1 position:%d", position);
                startActivity(new Intent(SurfaceViewActivity.this, SurfaceViewActivity.class));
                finish();
                overridePendingTransition(R.anim.out_from_left, R.anim.out_from_right);

//                fragmentManager.beginTransaction().add(R.id.frameLayout, new GuideFirstFragment(), "GuideFirstFragment").commit();
            }
            return true;
        } else if (e1.getX() - e2.getX() < -120) {
            YiLog.getInstance().i("Fling2");
            if (position > 0) {
                position--;
                YiLog.getInstance().i("Fling2 position:%d", position);
                startActivity(new Intent(SurfaceViewActivity.this, SurfaceViewActivity.class));
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.in_from_right);
//                fragmentManager.beginTransaction().add(R.id.frameLayout, new GuideFirstFragment(), "GuideFirstFragment").commit();
            }
            return true;
        }
        return true;
    }
}
