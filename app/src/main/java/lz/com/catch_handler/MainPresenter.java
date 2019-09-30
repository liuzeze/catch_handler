package lz.com.catch_handler;

import com.lz.fram.base.BaseView;
import com.lz.fram.base.RxPresenter;
import com.lz.fram.observer.BaseObserver;
import com.lz.fram.observer.JsonParseTransformer;
import com.lz.httplib.RxRequestUtils;
import com.lz.httplib.bean.SimpleParams;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author : liuze
 * @e-mail : 835052259@qq.com
 * @date : 2019/9/20-19:05
 * @desc : 修改内容
 * @version: 1.0
 */
public class MainPresenter extends RxPresenter<BaseView> {


    public void crash() {

        getCompose()
                .compose(this.<List<DataBean>>bindLifecycle())
                .subscribe(new BaseObserver<List<DataBean>>() {
                    @Override
                    public void onNext(List<DataBean> dataBeans) {
                        DataBean dataBean = dataBeans.get(0);
                        System.out.println(dataBean.getDesc() + "===" + dataBean.getTitle() + "liuze===========" + dataBean.getQqq());

                    }

                    @Override
                    protected void onError(int code, String mes) {
                        super.onError(code, mes);

                    }
                });

    }

    private Observable<List<DataBean>> getCompose() {

//        return ApiImpl.with((AppCompatActivity) mBaseView)
//                .get("https://www.wanandroid.com/banner/json", SimpleParams.create())
//                .compose(Transformer.switchSchedulersArray(DataBean.class));

        return RxRequestUtils
                .create()
                .get("https://www.wanandroid.com/banner/json", SimpleParams.create().put("",""))
                .compose(JsonParseTransformer.switchSchedulersArray(DataBean.class));
    }
}
