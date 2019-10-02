package com.lz.httplib.observer;

import android.os.SystemClock;
import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.lz.httplib.bean.ParseInfo;
import com.lz.httplib.callback.APICallBack;
import com.lz.httplib.http.GlobalConfigBuild;
import com.lz.httplib.http.HttpConfigFactory;
import com.lz.httplib.util.JSONFactory;

import org.json.JSONException;

import io.reactivex.functions.Function;

/**
 * @author : liuze
 * @e-mail : 835052259@qq.com
 * @date : 2019/9/26-20:15
 * @desc : 修改内容
 * @version: 1.0
 */
public class MapFunction implements Function<String, String> {

    @Override
    public String apply(String s) throws Exception {
        JsonElement jsonElement = JSONFactory.parseJson(s);
        JsonObject asJsonObject = jsonElement.getAsJsonObject();
        GlobalConfigBuild configBuild = HttpConfigFactory.getInstance().getConfigBuild();
        ParseInfo parseInfo = configBuild.getPaeseInfor();
        if (!parseInfo.hasKey(asJsonObject)) {
            throw new BaseException(new Throwable("Json格式化错误,ParseInfo不匹配"), ExceptionCode.PARSE_ERROR);

        }
        //拿到后台返回code
        String code = JSONFactory.getValue(jsonElement, parseInfo.getCodeKey());
        String msg = JSONFactory.getValue(jsonElement, parseInfo.getMsgKey());
        String data = JSONFactory.getValue(jsonElement, parseInfo.getDataKey());

        if (parseInfo.isSuccess(asJsonObject)) {
            return data;
        }
        APICallBack apiCallBack = configBuild.getApiCallBack();
        if (apiCallBack != null) {
            String callback = apiCallBack.callback(code, msg);
            if (!TextUtils.isEmpty(callback)) {
                msg = callback;
            }
        }
        throw new BaseException(new Throwable(msg), Integer.parseInt(code));


    }
}
