package com.hangzhou.santa.datatunnel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by santa on 2019/5/27.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface DataTunnelFilter {

    String tunnel() default "";

    String key();

    String[] accepts();

    boolean isPassThrough() default false;
}
