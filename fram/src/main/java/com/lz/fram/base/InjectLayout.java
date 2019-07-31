package com.lz.fram.base;

import com.lz.fram.R;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * -----------作者----------日期----------变更内容-----
 * -          刘泽      2019-04-03       创建class
 * @author Administrator
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InjectLayout {
    int layoutId();

    boolean isShowActTitle() default true;

    boolean isShowFragTitle() default false;

    boolean actBackIcon() default true;

    boolean fragBackIcon() default false;

    String titleName() default "标题";

}
