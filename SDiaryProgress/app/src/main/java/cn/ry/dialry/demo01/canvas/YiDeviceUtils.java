package cn.ry.dialry.demo01.canvas;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

public class YiDeviceUtils {
    private YiDeviceUtils() {

    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    public static int dip2px(Context context, float dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    public static int px2dip(Context context, float px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int getApiLevel() {
        return android.os.Build.VERSION.SDK_INT;
    }

    public static boolean isCompatible(int apiLevel) {
        return android.os.Build.VERSION.SDK_INT >= apiLevel;
    }

    public final static int getNumCores() {
        // Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                // Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }
        try {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            // Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            return Runtime.getRuntime().availableProcessors();
        }
    }

    public static boolean isInstalledOnSdCard(Context context) {
        // check for API level 8 and higher
        if (isCompatible(8)) {
            PackageManager pm = context.getPackageManager();
            try {
                PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
                ApplicationInfo ai = pi.applicationInfo;
                return (ai.flags & 0x00040000 /*
                                             * ApplicationInfo.
											 * FLAG_EXTERNAL_STORAGE
											 */) == 0x00040000 /*
                                                                 * ApplicationInfo.
																 * FLAG_EXTERNAL_STORAGE
																 */;
            } catch (NameNotFoundException e) {
                // ignore
            }
        }

        // check for API level 7 - check files dir
        try {
            String filesDir = context.getFilesDir().getAbsolutePath();
            if (filesDir.startsWith("/data/")) {
                return false;
            } else if (filesDir.contains(Environment
                    .getExternalStorageDirectory().getPath())) {
                return true;
            }
        } catch (Throwable e) {
            // ignore
        }

        return false;
    }
}
