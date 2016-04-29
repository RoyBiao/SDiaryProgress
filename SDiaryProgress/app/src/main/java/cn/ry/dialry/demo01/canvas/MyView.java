package cn.ry.dialry.demo01.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ruibiao on 16-4-28.
 */
public class MyView extends View {
    Context context;

    Paint textPaint;
    Paint linePaint;

    int screenWidth;
    int screenHeight;

    Point point;
    String desc;
    String brand;
    String price;

    boolean isInvalidate = false;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MyView(Context context) {
        super(context);
        this.context = context;
    }

    public void loadData(Point point, String desc, String brand, String price) {
        isInvalidate = true;
        screenWidth = YiDeviceUtils.getDisplayMetrics(context).widthPixels;
        screenHeight = YiDeviceUtils.getDisplayMetrics(context).heightPixels;
        this.point = point;
        this.desc = desc;
        this.brand = brand;
        this.price = price;
        linePaint = new Paint(); //设置一个笔刷大小是3的黄色的画笔
        linePaint.setColor(Color.YELLOW);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeWidth(3);
        textPaint = new Paint(); //设置一个笔刷大小是3的黄色的画笔
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(20);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInvalidate) {
            int x = point.x; //50
            int y = point.y; //100
            if (screenWidth - x < 200) {
                x = screenWidth - 200;
            }
            if (screenHeight - y < 100) {
                y = screenHeight - 100;
            }

            canvas.drawText(desc, x + 60, y - 55, textPaint);
            canvas.drawText(brand, x + 60, y - 5, textPaint);
            canvas.drawText(price, x + 60, y + 45, textPaint);

            Rect rect = new Rect();
            textPaint.getTextBounds(desc, 0, desc.length(), rect);
            int descW = rect.width();
            int descH = rect.height();

            textPaint.getTextBounds(brand, 0, brand.length(), rect);
            int brandW = rect.width();
            int brandH = rect.height();
            textPaint.getTextBounds(price, 0, price.length(), rect);
            int priceW = rect.width();
            int priceH = rect.height();

            canvas.drawCircle(x, y, 5, linePaint);

            canvas.drawLine(x, y, x + 50, y - 50, linePaint);
            canvas.drawLine(x + 50, y - 50, descW > 100 ? x + 50 + descW : x + 200, y - 50, linePaint);

            canvas.drawLine(x, y, brandW > 100 ? x + 50 + brandW : x + 200, y, linePaint);

            canvas.drawLine(x, y, x + 50, y + 50, linePaint);
            canvas.drawLine(x + 50, y + 50, priceW > 100 ? x + 50 + priceW : x + 200, y + 50, linePaint);
        }
//        canvas.drawCircle(50, 100, 10, linePaint);
//        canvas.drawLine(50, 100, 100, 50, linePaint);
//        canvas.drawLine(100, 50, 300, 50, linePaint);
//        canvas.drawLine(50, 100, 300, 100, linePaint);
//        canvas.drawLine(50, 100, 100, 150, linePaint);
//        canvas.drawLine(100, 150, 300, 150, linePaint);
//        canvas.drawText(desc, 110, 45, textPaint);
//        canvas.drawText(brand, 110, 95, textPaint);
//        canvas.drawText(price, 110, 145, textPaint);
    }

}
