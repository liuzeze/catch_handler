package lz.com.catch_handler;

import com.lz.fram.base.BaseView;
import com.lz.fram.base.RxPresenter;
import com.lz.fram.observer.CommonObserver;
import com.lz.fram.observer.Transformer;
import com.lz.httplib.RxHttp;
import com.lz.httplib.bean.SimpleParams;

import java.util.List;

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
                        System.out.println(dataBean.getDesc() );

                    }

                    @Override
                    protected void onError(int code, String mes) {
                        super.onError(code, mes);
                        System.out.println(code + "===liuze===========" + mes);

                    }

                });

    }


}
