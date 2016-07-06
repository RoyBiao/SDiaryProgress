package cn.ry.dialry.demo01.surface;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 转换工具
 * Created by ruibiao on 16-4-19.
 */
public class TransUtil {

    /**
     * 距离
     *
     * @param distance
     * @return
     */
    public static String getFormatDistance(String distance) {
        String back = "";
        if (!TextUtils.isEmpty(distance)) {
            double dis = Double.valueOf(distance);
            if (dis >= 100) {
                dis = dis / 1000;
                String parten = "#.##";
                DecimalFormat decimal = new DecimalFormat(parten);
                back = decimal.format(dis) + "km";
            } else {
                // holder.distance.setText((int) dis + "m");
                back = "100m";
            }
        }
        return back;
    }

    /**
     * 价格
     *
     * @param f
     * @return
     */
    public static float getFloat2Decimal(float f) {
        BigDecimal b = new BigDecimal(f);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 字符串转数组
     *
     * @param str
     * @return
     */
    public static String[] convertStrToArray(String str) {
        if (!TextUtils.isEmpty(str)) {
            return str.split(",");
        } else {
            return null;
        }
    }

    /**
     * 优惠卷折扣数
     *
     * @param cost
     * @return
     */
    public static String convertVolume(int cost) {
        int gewei = cost % 10;
        int shiwei = cost / 10;
        StringBuilder discount = new StringBuilder();
        if (gewei == 0) {
            discount.append(shiwei);
        } else {
            discount.append(shiwei);
            discount.append(".");
            discount.append(gewei);
        }
        return discount.toString();
    }

    public static AnimationDrawable loadBitmap(Context context,int[] frameRes) {
        AnimationDrawable clipDrawable = new AnimationDrawable();
        for (int i = 0; i < frameRes.length; i++) {
            //decodestream
            Bitmap bitmap = decodeSampledBitmapFromResource(context.getResources(),frameRes[i],620,623);
            clipDrawable.addFrame(new BitmapDrawable(bitmap), 150);
        }
        return clipDrawable;
    }

    /**
     * 获取合适的大小的图片的bitmap的形式
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    /**
     * 计算图片的缩放比例
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * @param str
     * @return
     */
    public static String replaceSpecialC(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str.replace("？", "%3F")
                .replace("&", "%26")
                .replace("|", "%124")
                .replace("=", "%3D")
                .replace("#", "%23")
                .replace("/", "%2F")
                .replace("+", "%2B")
                .replace("%", "%25")
                .replace(" ", "%20");
    }
}
