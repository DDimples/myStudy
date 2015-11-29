package com.mystudy.web.common.datasource.annotation;

import java.lang.annotation.*;


@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.PACKAGE})
public @interface DataSource {
    String value();
}
