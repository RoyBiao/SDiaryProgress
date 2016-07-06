package cn.ry.dialry.demo01.surface;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import cn.ry.dialry.R;

public class DemoSurfaceView extends SurfaceView implements Callback {
    int[] mFrameRes = new int[]{R.mipmap.mg1ani0001, R.mipmap.mg1ani0002, R.mipmap.mg1ani0003, R.mipmap.mg1ani0004, R.mipmap.mg1ani0005, R.mipmap.mg1ani0006, R.mipmap.mg1ani0007, R.mipmap.mg1ani0008, R.mipmap.mg1ani0009, R.mipmap.mg1ani0010, R.mipmap.mg1ani0011, R.mipmap.mg1ani0012};
    LoopThread2 thread;
    int i = 0;

    public DemoSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(); //初始化,设置生命周期回调方法
    }

    public DemoSurfaceView(Context context) {
        super(context);
        init(); //初始化,设置生命周期回调方法
    }

    private void init() {

        SurfaceHolder holder = getHolder();
        holder.addCallback(this); //设置Surface生命周期回调
        thread = new LoopThread2(holder, getContext());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.isRunning = true;
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

                try {
                    synchronized (surfaceHolder) {

                        c = surfaceHolder.lockCanvas(null);
                        doDraw(c);
                        //通过它来控制帧数执行一次绘制后休息50ms
                        Thread.sleep(50);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    surfaceHolder.unlockCanvasAndPost(c);
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
                        doDraw2(c);
                        //通过它来控制帧数执行一次绘制后休息50ms
                        Thread.sleep(150);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    surfaceHolder.unlockCanvasAndPost(c);
                }

            }

        }

        public void doDraw2(Canvas c) {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), mFrameRes[i % 12]);
            Bitmap dstbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                    bmp.getHeight(), null, true);
            c.drawColor(Color.BLACK);
            c.drawBitmap(dstbmp, 0, 0, null);
            i++;
        }
    }

}