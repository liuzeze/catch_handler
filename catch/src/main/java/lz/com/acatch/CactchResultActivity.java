
package lz.com.acatch;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class CactchResultActivity extends AppCompatActivity {
    private String pureStrCurrentErrorLog;

    @SuppressLint({"PrivateResource", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_error_activity);
        Toolbar mToolbarTb = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbarTb);

        TextView tvType = findViewById(R.id.tv_type);
        TextView tvMethodName = findViewById(R.id.tv_method_name);
        TextView tvLineNumber = findViewById(R.id.tv_line_number);
        TextView tvCause = findViewById(R.id.tv_cause);
        TextView tvStackTrace = findViewById(R.id.tv_stack_trace);
        Button button = findViewById(R.id.bt_sendservice);
        final ExceptionInfoBean exceptionInfoBean = getIntent().getParcelableExtra(CatchHandler.EXTRA_EXCEPTION_INFO);
        if (exceptionInfoBean == null) {
            return;
        }
        final String errorInfo = getExceptionInfoString(this, exceptionInfoBean);
        tvType.setText(Html.fromHtml("异常类型: <br/>" + getHtmlCodeBlueStr(exceptionInfoBean.getExceptionType())));
        String methodName = getHtmlCodeBlueStr(exceptionInfoBean.getClassName()) + "." + getHtmlCodeStr(exceptionInfoBean.getMethodName());
        tvMethodName.setText(Html.fromHtml("方法名: <br/>" + methodName));
        tvLineNumber.setText(Html.fromHtml("行号 : " + getHtmlCodeBlueStr(exceptionInfoBean.getLineNumber())));
        tvCause.setText(Html.fromHtml("Cause : " + getHtmlCodeBlueStr(exceptionInfoBean.getCause())));
        tvStackTrace.setText(errorInfo);

        if (exceptionInfoBean.AutoSend()) {
            sendServiceLog(errorInfo, exceptionInfoBean.getUrl(), exceptionInfoBean.getPostBody());
        } else {

            if (TextUtils.isEmpty(exceptionInfoBean.getUrl())) {
                button.setVisibility(View.GONE);
            } else {
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendServiceLog(errorInfo, exceptionInfoBean.getUrl(), exceptionInfoBean.getPostBody());
                    }
                });
            }
        }
        Button reboot = findViewById(R.id.bt_reboot);
        reboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = CactchResultActivity.this.getPackageManager()
                        .getLaunchIntentForPackage(CactchResultActivity.this.getApplication().getPackageName());
                startActivity(intent);

                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(10);
            }
        });
    }

    private void sendServiceLog(final String errorInfo, final String sendUrl, String body) {

        CatchHandlerHelper.upLoadErrorInfor(this, errorInfo, sendUrl, body);

    }

    private void saveErrorLogToFile(boolean isShowToast) {
        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (isSDPresent && isExternalStorageWritable()) {
            Date currentDate = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String strCurrentDate = dateFormat.format(currentDate);
            strCurrentDate = strCurrentDate.replace(" ", "_");
            String errorLogFileName = getApplicationName(CactchResultActivity.this) + "_Error-Log_" + strCurrentDate;
            String errorLog = getPureErrorDetailsFromIntent(CactchResultActivity.this, getIntent());
            String fullPath = Environment.getExternalStorageDirectory() + "/AppErrorLogs_UCEH/";
            FileOutputStream outputStream;
            try {
                File file = new File(fullPath);
                file.mkdir();
                File txtFile = new File(fullPath + errorLogFileName + ".txt");
                txtFile.createNewFile();
                outputStream = new FileOutputStream(txtFile);
                outputStream.write(errorLog.getBytes());
                outputStream.close();
                if (txtFile.exists() && isShowToast) {
                    Toast.makeText(this, "File Saved Successfully", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Log.e("REQUIRED", "This app does not have write storage permission to save log file.");
                if (isShowToast) {
                    Toast.makeText(this, "Storage Permission Not Found", Toast.LENGTH_SHORT).show();
                }
                e.printStackTrace();
            }
        }
    }

    private void shareErrorLog() {
        if (TextUtils.isEmpty(pureStrCurrentErrorLog)) {
            pureStrCurrentErrorLog = getPureErrorDetailsFromIntent(CactchResultActivity.this, getIntent());
        }
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Application Crash Error Log");
        share.putExtra(Intent.EXTRA_TEXT, pureStrCurrentErrorLog);
        startActivity(Intent.createChooser(share, "Share Error Log"));
    }

    private void copyErrorToClipboard() {
        String errorInformation = getPureErrorDetailsFromIntent(CactchResultActivity.this, getIntent());
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText("View Error Log", errorInformation);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(CactchResultActivity.this, "Error Log Copied", Toast.LENGTH_SHORT).show();
        }
    }

    private String getPureErrorDetailsFromIntent(Context context, Intent intent) {
        ExceptionInfoBean exceptionInfoBean = intent.getParcelableExtra(CatchHandler.EXTRA_EXCEPTION_INFO);
        if (TextUtils.isEmpty(pureStrCurrentErrorLog)) {
            StringBuilder errorReport = CatchHandlerHelper.getFullExceptionInfoString(context, exceptionInfoBean);
            pureStrCurrentErrorLog = errorReport.toString();
            return pureStrCurrentErrorLog;
        } else {
            return pureStrCurrentErrorLog;
        }
    }

    private String getExceptionInfoString(Context context, ExceptionInfoBean
            exceptionInfoBean) {
        return CatchHandlerHelper.getExceptionInfoString(context, exceptionInfoBean).toString();
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    private static String getHtmlCodeStr(String string) {
        return "<font color='#9976a8'>" + string + "</font>";
    }

    private static String getHtmlCodeBlueStr(String string) {
        return "<font color='#6a98b9'>" + string + "</font>";
    }

    private static String getHtmlCodeBlueStr(int value) {
        return getHtmlCodeBlueStr(Integer.toString(value));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crash_info_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_copy) {
            copyErrorToClipboard();
        } else if (i == R.id.action_save) {
            saveErrorLogToFile(true);
        } else if (i == R.id.action_share) {
            shareErrorLog();
        }
        return super.onOptionsItemSelected(item);
    }
}