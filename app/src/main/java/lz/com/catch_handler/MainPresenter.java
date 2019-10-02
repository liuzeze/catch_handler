package lz.com.catch_handler;

import com.lz.fram.base.BaseView;
import com.lz.fram.base.RxPresenter;
import com.lz.fram.observer.CommonObserver;
import com.lz.fram.observer.Transformer;
import com.lz.httplib.RxHttp;
import com.lz.httplib.bean.SimpleParams;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author : liuze
 * @e-mail : 835052259@qq.com
 * @date : 2019/9/20-19:05
 * @desc : 修改内容
 * @version: 1.0
 */
public class MainPresenter extends RxPresenter<BaseView> {


    public void crash() {

        RxHttp
                .create()
                .get("https://www.wanandroid.com/banner/json", SimpleParams.create().put("", ""))
                .compose(Transformer.switchSchedulersArray(DataBean.class))
                .subscribe(new CommonObserver<List<DataBean>>(mBaseView) {
                    @Override
                    public void onNext(List<DataBean> dataBeans) {
                        DataBean dataBean = dataBeans.get(0);
                        System.out.println(dataBean.getDesc());

                    }

                    @Override
                    protected void onError(int code, String mes) {
                        super.onError(code, mes);
                        System.out.println(code + "===" + mes);

                    }

                });

        RxHttp
                .create(ApiServer.class)
                .get("client_credentials",
                        "PPSSlAbaCYZpe8IhLQwWWinS",
                        "noqd2GKQmrpwhLu3hEuUi9X5cGQDfGw6")
                .compose(Transformer.<ResToken>switchThread())
                .subscribe(new CommonObserver<ResToken>(mBaseView) {
                    @Override
                    public void onNext(ResToken resToken) {
                        System.out.println(resToken.access_token);

                    }

                    @Override
                    protected void onError(int code, String mes) {
                        super.onError(code, mes);
                        System.out.println(code + "=====" + mes);

                    }

                });

        RxHttp
                .create()
                .get("https://aip.baidubce.com/oauth/2.0/token", SimpleParams.create()
                        .put("grant_type", "client_credentials")
                        .put("client_id", "PPSSlAbaCYZpe8IhLQwWWinS")
                        .put("client_secret", "noqd2GKQmrpwhLu3hEuUi9X5cGQDfGw6"))

                .compose(Transformer.switchSchedulersNoBase(ResToken.class))
                .observeOn(Schedulers.io())
                .flatMap(new Function<ResToken, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(ResToken resToken) throws Exception {
                        System.out.println("============Token 请求成功");
                        ReqToken reqToken = new ReqToken();
                        reqToken.image = "2164728647816478264781242746127846";
                        reqToken.image_type = "BASE64";
                        reqToken.face_field = "age,beauty,expression,faceshape,gender,glasses,landmark,race,qualities";
                        reqToken.max_face_num = "10";
                        reqToken.face_type = "LIVE";
                        reqToken.access_token = resToken.access_token;
                        return RxHttp
                                .create()
                                .post("https://aip.baidubce.com/rest/2.0/face/v3/detect", reqToken)
                                .compose(Transformer.<String>switchThread());
                    }
                })
                .subscribe(new CommonObserver<String>(mBaseView) {
                    @Override
                    public void onNext(String s) {
                        System.out.println("=======" + s);
                    }

                    @Override
                    protected void onError(int code, String mes) {
                        super.onError(code, mes);
                    }
                });

    }


}
