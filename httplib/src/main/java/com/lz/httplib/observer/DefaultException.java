package com.lz.httplib.observer;

import android.text.TextUtils;

import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.io.NotSerializableException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * -------- 日期 ---------- 维护人 ------------ 变更内容 --------
 * 2017/12/26	9:24	     刘泽			   全局异常统一处理
 */

public class DefaultException  {


    public static BaseException handleException(Throwable e) {
        BaseException ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new BaseException(httpException, httpException.code());
            try {
                if (TextUtils.isEmpty(httpException.message())) {
                    ex.setMessage(httpException.getMessage());
                } else {
                    ex.setMessage(httpException.message());
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                ex.setMessage(e1.getMessage());
            }
            return ex;
        } else if (e instanceof SocketTimeoutException) {
            ex = new BaseException(e, ExceptionCode.TIMEOUT_ERROR);
            ex.setMessage("网络连接超时，请检查您的网络状态，稍后重试！");
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new BaseException(e, ExceptionCode.TIMEOUT_ERROR);
            ex.setMessage("网络连接异常，请检查您的网络状态，稍后重试！");
            return ex;
        } else if (e instanceof ConnectTimeoutException) {
            ex = new BaseException(e, ExceptionCode.TIMEOUT_ERROR);
            ex.setMessage("网络连接超时，请检查您的网络状态，稍后重试！");
            return ex;
        } else if (e instanceof UnknownHostException) {
            ex = new BaseException(e, ExceptionCode.TIMEOUT_ERROR);
            ex.setMessage("网络连接异常，请检查您的网络状态，稍后重试！");
            return ex;
        } else if (e instanceof NullPointerException) {
            ex = new BaseException(e, ExceptionCode.NULL_POINTER_EXCEPTION);
            ex.setMessage("空指针异常");
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new BaseException(e, ExceptionCode.SSL_ERROR);
            ex.setMessage("证书验证失败");
            return ex;
        } else if (e instanceof ClassCastException) {
            ex = new BaseException(e, ExceptionCode.CAST_ERROR);
            ex.setMessage("类型转换错误");
            return ex;
        } else if (
                e instanceof JSONException
                        || e instanceof NotSerializableException) {
            ex = new BaseException(e, ExceptionCode.PARSE_ERROR);
            ex.setMessage("解析错误");
            return ex;
        } else if (e instanceof IllegalStateException) {
            ex = new BaseException(e, ExceptionCode.ILLEGAL_STATE_ERROR);
            ex.setMessage(e.getMessage());
            return ex;
        } else {
            ex = new BaseException(e, ExceptionCode.UNKNOWN);
            ex.setMessage("未知错误" + e.getMessage());
            return ex;
        }
    }

}
