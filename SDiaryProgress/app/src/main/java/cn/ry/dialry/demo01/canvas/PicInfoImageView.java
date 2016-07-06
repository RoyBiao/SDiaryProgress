package cn.ry.dialry.demo01.canvas;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import cn.ry.dialry.R;

/**
 * Created by ruibiao on 16-4-28.
 */
public class PicInfoImageView extends FrameLayout {


    public PicInfoImageView(Context context) {
        super(context);
        initView(context);
    }

    public PicInfoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        int width = YiDeviceUtils.getDisplayMetrics(context).widthPixels;
        int height = YiDeviceUtils.getDisplayMetrics(context).widthPixels;
        ViewGroup.LayoutParams imageLP = new ViewGroup.LayoutParams(width,height);

        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(imageLP);
        imageView.setImageResource(R.mipmap.liutao);
        addView(imageView);

        MyView myView = new MyView(context);
        myView.setLayoutParams(imageLP);
        myView.loadData(width, height, new Point(700, 900), "desc", "brand", "price");
        addView(myView);

    }

}
