package lz.com.acatch;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CatchHandlerHelper {

    private static final int MAX_STACK_TRACE_SIZE = 131071; //128 KB - 1
    private static final String LINE_SEPARATOR = "\n";

    public static ExceptionInfoBean getExceptionInfoBean(Throwable throwable, String errorPath) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        String stackTraceString = sw.toString();
        if (stackTraceString.length() > MAX_STACK_TRACE_SIZE) {
            String disclaimer = " [stack trace too large]";
            stackTraceString = stackTraceString.substring(0, MAX_STACK_TRACE_SIZE - disclaimer.length()) + disclaimer;
        }

        Throwable rootTr = throwable;
        String cause = throwable.getMessage();
        while (throwable.getCause() != null) {
            throwable = throwable.getCause();
            if (throwable.getStackTrace() != null && throwable.getStackTrace().length > 0) {
                rootTr = throwable;
            }
            String msg = throwable.getMessage();
            if (!TextUtils.isEmpty(msg))
                cause = msg;
        }

        String exceptionType = rootTr.getClass().getName();

        String throwClassName;
        String throwMethodName;
        int throwLineNumber;

        if (rootTr.getStackTrace().length > 0) {
            StackTraceElement trace = rootTr.getStackTrace()[0];
            throwClassName = trace.getClassName();
            throwMethodName = trace.getMethodName();
            throwLineNumber = trace.getLineNumber();
        } else {
            throwClassName = "unknown";
            throwMethodName = "unknown";
            throwLineNumber = -1;
        }

        StringBuilder activityLogStringBuilder = new StringBuilder();

        return ExceptionInfoBean.newInstance()
                .setErrorLogPath(errorPath)
                .cause(cause)
                .className(throwClassName)
                .methodName(throwMethodName)
                .exceptionType(exceptionType)
                .lineNumber(throwLineNumber)
                .stackTraceString(stackTraceString)
                .activityLogString(activityLogStringBuilder.toString());
    }


    public static StringBuilder getExceptionInfoString(Context context, ExceptionInfoBean exceptionInfoBean) {
        return getExceptionInfoString(context, exceptionInfoBean, false);
    }

    static StringBuilder getFullExceptionInfoString(Context context, ExceptionInfoBean exceptionInfoBean) {
        return getExceptionInfoString(context, exceptionInfoBean, true);
    }

    @NonNull
    private static StringBuilder getExceptionInfoString(Context context, ExceptionInfoBean exceptionInfoBean, boolean useHeader) {
        StringBuilder errorReport = new StringBuilder();
        if (useHeader) {
            getExceptionHeaderInfo(exceptionInfoBean, errorReport);
        }
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n------------ 异常信息 ------------\n");
        errorReport.append(LINE_SEPARATOR);
        if (exceptionInfoBean != null) {
            errorReport.append(exceptionInfoBean.getStackTraceString());
        }
        errorReport.append(LINE_SEPARATOR);
        if (exceptionInfoBean != null) {
            errorReport.append(exceptionInfoBean.getActivityLogString());
            errorReport.append(LINE_SEPARATOR);
        }
        errorReport.append("\n------------ 设备信息 ------------\n");
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Manufacturer: ");
        errorReport.append(Build.MANUFACTURER);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n------------ APP 信息 ------------\n");
        errorReport.append(LINE_SEPARATOR);
        String versionName = getVersionName(context);
        errorReport.append("Version: ");
        errorReport.append(versionName);
        errorReport.append(LINE_SEPARATOR);
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String firstInstallTime = getFirstInstallTimeAsString(context, dateFormat);
        if (!TextUtils.isEmpty(firstInstallTime)) {
            errorReport.append("Installed On: ");
            errorReport.append(firstInstallTime);
            errorReport.append(LINE_SEPARATOR);
        }
        String lastUpdateTime = getLastUpdateTimeAsString(context, dateFormat);
        if (!TextUtils.isEmpty(lastUpdateTime)) {
            errorReport.append("Updated On: ");
            errorReport.append(lastUpdateTime);
            errorReport.append(LINE_SEPARATOR);
        }
        errorReport.append("Current Date: ");
        errorReport.append(dateFormat.format(currentDate));
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n");
        return errorReport;
    }

    private static void getExceptionHeaderInfo(ExceptionInfoBean exceptionInfoBean, @NonNull StringBuilder errorReport) {
        errorReport.append("------------ CATCH HANDLER Library ------------");
        errorReport.append("\n------------ by NeXT ------------\n");
        errorReport.append(LINE_SEPARATOR);
        if (exceptionInfoBean != null) {
            errorReport.append("Exception Type: ");
            errorReport.append(LINE_SEPARATOR);
            errorReport.append(exceptionInfoBean.getExceptionType());
            errorReport.append(LINE_SEPARATOR);
            errorReport.append(LINE_SEPARATOR);
            errorReport.append("Class Name: ");
            errorReport.append(LINE_SEPARATOR);
            errorReport.append(exceptionInfoBean.getClassName()).append(".").append(exceptionInfoBean.getMethodName());
            errorReport.append(LINE_SEPARATOR);
            errorReport.append(LINE_SEPARATOR);
            errorReport.append("LineNumber: ").append(exceptionInfoBean.getLineNumber());
            errorReport.append(LINE_SEPARATOR);
            errorReport.append(LINE_SEPARATOR);
            errorReport.append("Cause: ");
            errorReport.append(LINE_SEPARATOR);
            errorReport.append(exceptionInfoBean.getCause());
            errorReport.append(LINE_SEPARATOR);
        }
    }

    private static String getVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private static String getFirstInstallTimeAsString(Context context, DateFormat dateFormat) {
        long firstInstallTime;
        try {
            firstInstallTime = context
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .firstInstallTime;
            return dateFormat.format(new Date(firstInstallTime));
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    private static String getLastUpdateTimeAsString(Context context, DateFormat dateFormat) {
        long lastUpdateTime;
        try {
            lastUpdateTime = context
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .lastUpdateTime;
            return dateFormat.format(new Date(lastUpdateTime));
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }


    public static void upLoadErrorInfor(final Context context, final String sendUrl, final String postBody) {
        if (!TextUtils.isEmpty(postBody)) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String body = postBody.replace(BaseSendError.BODYSTR, postBody);

                        URL url = new URL(sendUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");//设置请求方式为POST
                        connection.setDoOutput(true);
                        //允许写出
                        connection.setDoInput(true);
                        // 允许读入
                        connection.setUseCaches(false);
                        // 不使用缓存
                        connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                        connection.connect();
                        // 连接

                        //设置参数类型是json格式
                        connection.connect();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                        writer.write(body);
                        writer.close();

                        int responseCode = connection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
                            ((Activity) context).finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

        }
    }

}
