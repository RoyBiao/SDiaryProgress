package cn.ry.dialry.demo01.surface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.widget.LinearLayout;

import cn.ry.dialry.R;
import cn.ry.dialry.util.YiDeviceUtils;
import cn.ry.dialry.util.YiLog;

/**
 * Created by ruibiao on 16-7-9.
 */
public class DemoextureView extends TextureView implements TextureView.SurfaceTextureListener {
    int[] mFrameRes = new int[]{R.mipmap.mg1ani0001, R.mipmap.mg1ani0002, R.mipmap.mg1ani0003, R.mipmap.mg1ani0004, R.mipmap.mg1ani0005, R.mipmap.mg1ani0006, R.mipmap.mg1ani0007, R.mipmap.mg1ani0008, R.mipmap.mg1ani0009, R.mipmap.mg1ani0010, R.mipmap.mg1ani0011, R.mipmap.mg1ani0012, R.mipmap.mg4ani0001, R.mipmap.mg4ani0002, R.mipmap.mg4ani0003, R.mipmap.mg4ani0004, R.mipmap.mg4ani0005, R.mipmap.mg4ani0006, R.mipmap.mg4ani0007, R.mipmap.mg4ani0008, R.mipmap.mg4ani0009, R.mipmap.mg4ani0010, R.mipmap.mg4ani0011, R.mipmap.mg4ani0012};
    LinearLayout.LayoutParams layoutParams = null;
    LoopThread2 thread;
    int i = 0;

    public DemoextureView(Context context) {
        super(context);
    }

    public DemoextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init() {
//        SurfaceHolder holder = getHolder();
//        holder.addCallback(this); //设置Surface生命周期回调
        thread = new LoopThread2(null, getContext());
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        thread.isRunning = true;
        thread.start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        thread.isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    /**
     * 执行绘制的绘制线程
     *
     * @author Administrator
     */
    class LoopThread extends Thread {

        SurfaceHolder surfaceHolder;
        Context context;
        boolean isRunning;
        float radius = 10f;
        Paint paint;

        public LoopThread(SurfaceHolder surfaceHolder, Context context) {

            this.surfaceHolder = surfaceHolder;
            this.context = context;
            isRunning = false;

            paint = new Paint();
            paint.setColor(Color.YELLOW);
            paint.setStyle(Paint.Style.STROKE);
        }

        @Override
        public void run() {

            Canvas c = null;

            while (isRunning) {
                YiLog.getInstance().i("isRunning");
                try {
                    synchronized (surfaceHolder) {
                        c = surfaceHolder.lockCanvas(null);
                        if (c != null) {
                            doDraw(c);
                            //通过它来控制帧数执行一次绘制后休息50ms
                            Thread.sleep(50);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }

            }

        }

        public void doDraw(Canvas c) {

            //这个很重要，清屏操作，清楚掉上次绘制的残留图像
            c.drawColor(Color.BLACK);

            c.translate(200, 200);
            c.drawCircle(0, 0, radius++, paint);

            if (radius > 100) {
                radius = 10f;
            }

        }

    }

    /**
     * 执行绘制的绘制线程
     *
     * @author Administrator
     */
    class LoopThread2 extends Thread {

        SurfaceHolder surfaceHolder;
        Context context;
        boolean isRunning;

        public LoopThread2(SurfaceHolder surfaceHolder, Context context) {

            this.surfaceHolder = surfaceHolder;
            this.context = context;
            isRunning = false;

        }

        @Override
        public void run() {

            Canvas c = null;

            while (isRunning) {

                try {
                    synchronized (surfaceHolder) {
                        c = surfaceHolder.lockCanvas(null);
                        if (c != null) {
                            doDraw2(c);
                            //通过它来控制帧数执行一次绘制后休息50ms
                            Thread.sleep(100);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }

            }

        }

        public void doDraw2(Canvas c) {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), mFrameRes[i % 24]);
            if (layoutParams == null) {
                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, bmp.getHeight());
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        setLayoutParams(layoutParams);
                    }
                });

            }

            int left = (YiDeviceUtils.getDisplayMetrics(getContext()).widthPixels - bmp.getWidth()) / 2;
            Bitmap dstbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                    bmp.getHeight(), null, true);
            c.drawColor(Color.WHITE);
            c.drawBitmap(dstbmp, left, 0, null);
            i++;
        }
    }
}
