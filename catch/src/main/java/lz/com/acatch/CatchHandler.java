
package lz.com.acatch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;

public final class CatchHandler {
    static final String EXTRA_EXCEPTION_INFO = "EXTRA_EXCEPTION_INFO";
    private final static String TAG = "CatchHandler";
    private static final String UCE_HANDLER_PACKAGE_NAME = "lz.com.acatch";
    private static final String DEFAULT_HANDLER_PACKAGE_NAME = "com.android.internal.os";
    private static final String SHARED_PREFERENCES_FILE = "catch_preferences";
    private static final String SHARED_PREFERENCES_FIELD_TIMESTAMP = "last_crash_timestamp";
    private Application application;
    private boolean isCatchHEnabled;
    private CatchCallback mCatchCallback = null;

    private CatchHandler(Builder builder) {
        mCatchCallback = builder.mCatchCallback;
        isCatchHEnabled = builder.isCatchHEnabled;
        CatchHandlerHelper.setPostBodyStyle(builder.mBaseSendError.sendErrorStr());
        CatchHandlerHelper.setServiceUrl(builder.url, builder.isAutoSend);
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

    private void setDefaultUncaughtExceptionHandler(final Thread.UncaughtExceptionHandler oldHandler) {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                if (isCatchHEnabled) {
                    if (hasCrashedInTheLastSeconds(application)) {
                        if (oldHandler != null) {
                            oldHandler.uncaughtException(thread, throwable);
                            return;
                        }
                    } else {
                        setLastCrashTimestamp(application, new Date().getTime());
                        ExceptionInfoBean exceptionInfoBean = CatchHandlerHelper.getExceptionInfoBean(throwable);
                        if (mCatchCallback != null) {
                            mCatchCallback.exceptionInfo(exceptionInfoBean);
                            mCatchCallback.throwable(throwable);
                            return;
                        }
                        final Intent intent = new Intent(application, CactchResultActivity.class);
                        intent.putExtra(EXTRA_EXCEPTION_INFO, exceptionInfoBean);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        application.startActivity(intent);

                    }

                    killCurrentProcess();

                } else if (oldHandler != null) {
                    oldHandler.uncaughtException(thread, throwable);
                }
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
        private Context context;
        private boolean isCatchHEnabled = true;
        private String url = "";
        private boolean isAutoSend = false;
        private CatchCallback mCatchCallback = null;
        private BaseSendError mBaseSendError=new SendDingDing();

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setCatchHEnabled(boolean catchHEnabled) {
            isCatchHEnabled = catchHEnabled;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setAutoSend(boolean autoSend) {
            isAutoSend = autoSend;
            return this;
        }

        public Builder setCatchCallback(CatchCallback catchCallback) {
            mCatchCallback = catchCallback;
            return this;
        }

        public Builder setBaseSendError(BaseSendError baseSendError) {
            mBaseSendError = baseSendError;
            return this;
        }

        public CatchHandler build() {
            return isCatchHEnabled ? new CatchHandler(this) : null;
        }
    }
}
