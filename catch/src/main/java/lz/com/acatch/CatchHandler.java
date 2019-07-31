
package lz.com.acatch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

public final class CatchHandler {
    static final String EXTRA_EXCEPTION_INFO = "EXTRA_EXCEPTION_INFO";
    private final static String TAG = "CatchHandler";
    private static final String UCE_HANDLER_PACKAGE_NAME = "lz.com.acatch";
    private static final String DEFAULT_HANDLER_PACKAGE_NAME = "com.android.internal.os";
    private static final String SHARED_PREFERENCES_FILE = "catch_preferences";
    private static final String SHARED_PREFERENCES_FIELD_TIMESTAMP = "last_crash_timestamp";
    private final String mErrorPath;
    private Application application;
    private boolean isCatchHEnabled;
    private CatchCallback mCatchCallback = null;

    private CatchHandler(Builder builder) {
        mCatchCallback = builder.mCatchCallback;
        isCatchHEnabled = builder.isCatchHEnabled;
        mErrorPath = builder.mErrorPath;
        setUCEHandler(builder.context);
    }

    private void setUCEHandler(final Context context) {
        try {
            if (context != null) {
                final Thread.UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();
                if (oldHandler != null && oldHandler.getClass().getName().startsWith(UCE_HANDLER_PACKAGE_NAME)) {
                    Log.e(TAG, "UCEHandler was already installed, doing nothing!");
                } else {
                    if (oldHandler != null && !oldHandler.getClass().getName().startsWith(DEFAULT_HANDLER_PACKAGE_NAME)) {
                        Log.e(TAG, "You already have an UncaughtExceptionHandler. If you use a custom UncaughtExceptionHandler, it should be initialized after UCEHandler! Installing anyway, but your original handler will not be called.");
                    }
                    application = (Application) context.getApplicationContext();
                    setDefaultUncaughtExceptionHandler(oldHandler);
                }
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "CatchHandler can not be initialized. Help making it better by reporting this as a bug.", throwable);
        }
    }

    /**
     * 拦截系统异常
     *
     * @param oldHandler
     */
    private void setDefaultUncaughtExceptionHandler(final Thread.UncaughtExceptionHandler oldHandler) {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Toast.makeText(application,"程序异常,退出应用",Toast.LENGTH_SHORT).show();
                if (isCatchHEnabled) {
                    if (hasCrashedInTheLastSeconds(application)) {
                        if (oldHandler != null) {
                            oldHandler.uncaughtException(thread, throwable);
                            return;
                        }
                    } else {
                        setLastCrashTimestamp(application, new Date().getTime());
                        ExceptionInfoBean exceptionInfoBean = CatchHandlerHelper.getExceptionInfoBean(throwable,mErrorPath);
                        if (mCatchCallback != null) {
                            mCatchCallback.exceptionInfo(exceptionInfoBean, throwable);
                            return;
                        }
                        final Intent intent = new Intent(application, CactchResultActivity.class);
                        intent.putExtra(EXTRA_EXCEPTION_INFO, exceptionInfoBean);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        application.startActivity(intent);

                    }


                } else if (oldHandler != null) {
                    oldHandler.uncaughtException(thread, throwable);
                }
                killCurrentProcess();
            }
        });
    }


    private static boolean hasCrashedInTheLastSeconds(Context context) {
        long lastTimestamp = getLastCrashTimestamp(context);
        long currentTimestamp = new Date().getTime();
        return (lastTimestamp <= currentTimestamp && currentTimestamp - lastTimestamp < 3000);
    }

    @SuppressLint("ApplySharedPref")
    private static void setLastCrashTimestamp(Context context, long timestamp) {
        context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putLong(SHARED_PREFERENCES_FIELD_TIMESTAMP, timestamp).commit();
    }

    public static void killCurrentProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    private static long getLastCrashTimestamp(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE).getLong(SHARED_PREFERENCES_FIELD_TIMESTAMP, -1);
    }

    static void closeApplication(Activity activity) {
        activity.finish();
        killCurrentProcess();
    }

    public static class Builder {
        //上下文
        private Context context;
        //是否开启本地捕获
        private boolean isCatchHEnabled = true;
        //异常捕获回调
        private CatchCallback mCatchCallback = null;
        //错误日志保存路径
        private String mErrorPath;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setCatchHEnabled(boolean catchHEnabled) {
            isCatchHEnabled = catchHEnabled;
            return this;
        }

        public Builder setCatchCallback(CatchCallback catchCallback) {
            mCatchCallback = catchCallback;
            return this;
        }
        public CatchHandler build() {
            return new CatchHandler(this);
        }

        public Builder setErrorLogPath(String errorPath) {
            mErrorPath = errorPath;
            return this;
        }
    }
}
