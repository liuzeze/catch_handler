package lz.com.acatch;

import androidx.annotation.Nullable;


public interface CatchCallback {

    void exceptionInfo(@Nullable ExceptionInfoBean exceptionInfoBean,@Nullable Throwable throwable);


}
