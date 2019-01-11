/*
 *
 *  * Copyright © 2018 Rohit Sahebrao Surwase.
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package lz.com.acatch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;

public final class UCEHandler {
    static final String EXTRA_EXCEPTION_INFO = "EXTRA_EXCEPTION_INFO";
    private final static String TAG = "UCEHandler";
    private static final String UCE_HANDLER_PACKAGE_NAME = "me.next.me.next.uceh";
    private static final String DEFAULT_HANDLER_PACKAGE_NAME = "com.android.internal.os";
    private static final String SHARED_PREFERENCES_FILE = "uceh_preferences";
    private static final String SHARED_PREFERENCES_FIELD_TIMESTAMP = "last_crash_timestamp";
    private static final int MAX_ACTIVITIES_IN_LOG = 50;
    private Application application;
    private boolean isBackgroundMode;
    private boolean isUCEHEnabled;
    private UCECallback mUCECallback = null;

    private UCEHandler(Builder builder) {
        mUCECallback = builder.mUCECallback;
        isUCEHEnabled = builder.isUCEHEnabled;
        isBackgroundMode = builder.isBackgroundModeEnabled;
        UCEHandlerHelper.setServiceUrl(builder.url, builder.isAutoSend);
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
                    //Setup UCE Handler.
                    setDefaultUncaughtExceptionHandler(oldHandler);
                }
                Log.i(TAG, "UCEHandler has been installed.");
            } else {
                Log.e(TAG, "Context can not be null");
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "UCEHandler can not be initialized. Help making it better by reporting this as a bug.", throwable);
        }
    }

    private void setDefaultUncaughtExceptionHandler(final Thread.UncaughtExceptionHandler oldHandler) {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                if (isUCEHEnabled) {
                    Log.e(TAG, "App crashed, executing UCEHandler's UncaughtExceptionHandler", throwable);
                    if (hasCrashedInTheLastSeconds(application)) {
                        Log.e(TAG, "App already crashed recently, not starting custom error activity because we could enter a restart loop. Are you sure that your app does not crash directly on init?", throwable);
                        if (oldHandler != null) {
                            oldHandler.uncaughtException(thread, throwable);
                            return;
                        }
                    } else {
                        setLastCrashTimestamp(application, new Date().getTime());
                        if (isBackgroundMode) {
                            ExceptionInfoBean exceptionInfoBean = UCEHandlerHelper.getExceptionInfoBean(throwable);
                            if (mUCECallback != null) {
                                mUCECallback.exceptionInfo(exceptionInfoBean);
                                mUCECallback.throwable(throwable);
                                return;
                            }
                            final Intent intent = new Intent(application, UCEDefaultActivity.class);
                            intent.putExtra(EXTRA_EXCEPTION_INFO, exceptionInfoBean);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            application.startActivity(intent);
                        } else {
                            if (oldHandler != null) {
                                oldHandler.uncaughtException(thread, throwable);
                                return;
                            }
                            //If it is null (should not be), we let it continue and kill the process or it will be stuck
                        }
                    }

                    killCurrentProcess();

                } else if (oldHandler != null) {
                    //Pass control to old uncaught exception handler
                    oldHandler.uncaughtException(thread, throwable);
                }
            }
        });
    }


    /**
     * INTERNAL method that tells if the app has crashed in the last seconds.
     * This is used to avoid restart loops.
     *
     * @return true if the app has crashed in the last seconds, false otherwise.
     */
    private static boolean hasCrashedInTheLastSeconds(Context context) {
        long lastTimestamp = getLastCrashTimestamp(context);
        long currentTimestamp = new Date().getTime();
        return (lastTimestamp <= currentTimestamp && currentTimestamp - lastTimestamp < 3000);
    }

    @SuppressLint("ApplySharedPref")
    private static void setLastCrashTimestamp(Context context, long timestamp) {
        context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putLong(SHARED_PREFERENCES_FIELD_TIMESTAMP, timestamp).commit();
    }

    private static void killCurrentProcess() {
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
        private boolean isUCEHEnabled = true;
        private boolean isBackgroundModeEnabled = true;
        private String url = "";
        private boolean isAutoSend = false;
        private UCECallback mUCECallback = null;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setUCEHCallback(UCECallback ucehCallback) {
            this.mUCECallback = ucehCallback;
            return this;
        }

        public Builder setUCEHEnabled(boolean isUCEHEnabled) {
            this.isUCEHEnabled = isUCEHEnabled;
            return this;
        }

        public Builder setBackgroundModeEnabled(boolean isBackgroundModeEnabled) {
            this.isBackgroundModeEnabled = isBackgroundModeEnabled;
            return this;
        }

        public UCEHandler build() {
            return new UCEHandler(this);
        }

        public Builder setServiceUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setAutoSendLog(boolean isAutoSend) {
            this.isAutoSend = isAutoSend;
            return this;
        }
    }
}
