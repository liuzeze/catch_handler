package com.lz.httplib.observer;

/**
 * @author : liuze
 * @e-mail : 835052259@qq.com
 * @date : 2019/10/2-10:30
 * @desc : 修改内容
 * @version: 1.0
 */
public interface ExceptionCode {
    /**
     * 未知错误
     */
   int UNKNOWN = -1000;
    /**
     * 连接超时
     */
   int TIMEOUT_ERROR = -1001;
    /**
     * 空指针错误
     */
   int NULL_POINTER_EXCEPTION = -1002;

    /**
     * 证书出错
     */
   int SSL_ERROR = -1003;

    /**
     * 类转换错误
     */
   int CAST_ERROR = -1004;

    /**
     * 解析错误
     */
   int PARSE_ERROR = -1005;

    /**
     * 非法数据异常
     */
   int ILLEGAL_STATE_ERROR = -1006;
}
