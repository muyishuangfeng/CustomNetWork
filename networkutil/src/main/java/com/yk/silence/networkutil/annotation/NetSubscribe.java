package com.yk.silence.networkutil.annotation;

import com.yk.silence.networkutil.type.Mode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NetSubscribe {
    Mode mode() default Mode.AUTO;
}
