package cn.ry.dialry.util;

import android.util.Log;

import java.util.Formatter;

/**
 * 全局Log类，用于合理控制LOG，便于debug和release 只查看通过YiLog打出来的log的命令是，adb logcat -s YiLog
 * 如果用的不是单例的YiLog，只查看通过YiLog打出来的日志需要结合实例YiLog时的TAG 就像这样，adb logcat -s TAG
 *
 * @author biao
 */
public class YiLog {
    // release版本时，记得把不必要的log关闭
    public static boolean ENABLE_DEBUG = true;
    public static boolean ENABLE_INFO = true;
    public static boolean ENABLE_ERROR = true;
    public static boolean ENABLE_WARN = true;
    public static boolean ENABLE_VERBOSE = true;

    private static YiLog instance = null;

    public static YiLog getInstance() {
        if (instance == null) {
            instance = new YiLog("YiLog");
        }
        return instance;
    }

    public YiLog(String tag) {
        if (tag == null) {
            throw new NullPointerException("tag non-null");
        }
        this.tag = tag;
    }

    private String tag = "YiLog";

    /**
     * A little trick to reuse a formatter in the same thread
     */
    private static class ReusableFormatter {

        private Formatter formatter;
        private StringBuilder builder;

        public ReusableFormatter() {
            builder = new StringBuilder();
            formatter = new Formatter(builder);
        }

        public String format(String msg, Object... args) {
            formatter.format(msg, args);
            String s = builder.toString();
            builder.setLength(0);
            return s;
        }
    }

    private static final ThreadLocal<ReusableFormatter> thread_local_formatter = new ThreadLocal<ReusableFormatter>() {
        protected ReusableFormatter initialValue() {
            return new ReusableFormatter();
        }
    };

    public String format(String msg, Object... args) {
        ReusableFormatter formatter = thread_local_formatter.get();
        return formatter.format(msg, args);
    }

    public void debug(String msg) {
        if (ENABLE_DEBUG) {
            Log.d(getFunctionName(), msg);
        }
    }

    public void debug(String msg, Object... args) {
        if (ENABLE_DEBUG) {
            Log.d(getFunctionName(), format(msg, args));
        }
    }

    public void debug(Throwable err, String msg, Object... args) {
        if (ENABLE_DEBUG) {
            Log.d(getFunctionName(), format(msg, args), err);
        }
    }

    public void d(String msg) {
        if (ENABLE_DEBUG) {
            Log.d(getFunctionName(), msg);
        }
    }

    public void d(String msg, Object... args) {
        if (ENABLE_DEBUG) {
            Log.d(getFunctionName(), format(msg, args));
        }
    }

    public void d(Throwable err, String msg, Object... args) {
        if (ENABLE_DEBUG) {
            Log.d(getFunctionName(), format(msg, args), err);
        }
    }

    public void i(String msg) {
        if (ENABLE_INFO) {
            Log.i(getFunctionName(), msg);
        }
    }

    public void i(String msg, Object... args) {
        if (ENABLE_INFO) {
            Log.i(getFunctionName(), format(msg, args));
        }
    }

    public void i(Throwable err, String msg, Object... args) {
        if (ENABLE_INFO) {
            Log.i(getFunctionName(), format(msg, args), err);
        }
    }

    public void w(String msg) {
        if (ENABLE_WARN) {
            Log.w(getFunctionName(), msg);
        }
    }

    public void w(String msg, Object... args) {
        if (ENABLE_WARN) {
            Log.w(getFunctionName(), format(msg, args));
        }
    }

    public void w(Throwable err, String msg, Object... args) {
        if (ENABLE_WARN) {
            Log.w(getFunctionName(), format(msg, args), err);
        }
    }

    public void e(String msg) {
        if (ENABLE_ERROR) {
            Log.e(getFunctionName(), msg);
        }
    }

    public void e(String msg, Object... args) {
        if (ENABLE_ERROR) {
            Log.e(getFunctionName(), format(msg, args));
        }
    }

    public void e(Throwable err, String msg, Object... args) {
        if (ENABLE_ERROR) {
            Log.e(getFunctionName(), format(msg, args), err);
        }
    }

    public void v(String msg) {
        if (ENABLE_VERBOSE) {
            Log.v(getFunctionName(), msg);
        }
    }

    public void v(String msg, Object... args) {
        if (ENABLE_VERBOSE) {
            Log.v(getFunctionName(), format(msg, args));
        }
    }

    public void v(Throwable err, String msg, Object... args) {
        if (ENABLE_VERBOSE) {
            Log.v(getFunctionName(), format(msg, args), err);
        }
    }

    /**
     * Get The Current Function Name
     *
     * @return
     */
    private String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(this.getClass().getName())) {
                continue;
            }
            return tag + "[" + Thread.currentThread().getName() + ":"
                    + st.getFileName() + ":"
                    + st.getMethodName() + ":" + st.getLineNumber() + "]";
        }
        return null;
    }
}
