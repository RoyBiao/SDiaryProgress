package com.ikantech.support.util;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

public class YiFileUtils {
    public static final String FILE_SUFFIX = ".ik";

    private static String mStorePath = null;
    private static Context mContext = null;

    private YiFileUtils() {
    }

    public static void register(Context context) {
        mContext = context;
    }

    /**
     * 缓存数据的存放位置为:/sdcard
     * 如果SD卡不存在时缓存存放位置为:/data/data/
     *
     * @return
     */
    public static String getStorePath() {
        if (mStorePath == null) {
            String path;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                    !Environment.isExternalStorageRemovable()) {
                if(mContext.getExternalCacheDir()!=null)
                    path = mContext.getExternalCacheDir().getPath();
                else
                    path = mContext.getCacheDir().getPath();
            } else {
                path = mContext.getCacheDir().getPath();
            }
            if (!path.endsWith("/")) {
                path = path + "/";
            }
            mStorePath = path;
        }
        return mStorePath;
    }

    /**
     * 删除文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        deleteFile(file2);
                    } else {
                        file2.deleteOnExit();
                    }
                }
            }
            file.deleteOnExit();
        }
    }
}
