package lz.com.acatch;

import android.support.annotation.Nullable;


public interface CatchCallback {

    void exceptionInfo(@Nullable ExceptionInfoBean exceptionInfoBean,@Nullable Throwable throwable);


}
