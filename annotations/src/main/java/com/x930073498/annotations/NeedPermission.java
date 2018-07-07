/*
 * Permissions      2016-05-12
 * Copyright (c) 2016 hujiang Co.Ltd. All right reserved(http://www.hujiang.com).
 *
 */
package com.x930073498.annotations;

import com.x930073498.permission.PermissionsProxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * to mark the class type or method that needs runtime permissions.Just used on Activity class, and all void returned methods of any class.
 *
 * @author simon
 * @version 1.0.0
 * @since 2016-05-12
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedPermission {

    Class<? extends PermissionsProxy> impl() default PermissionsProxy.class;

    int[] requestCode() default 0;


    String[] permissions() default "";

    String rationalMessage() default "";

    int rationalMsgResId() default 0;

    String rationalButton() default "";

    int rationalBtnResId() default 0;

    String deniedMessage() default "";

    int deniedMsgResId() default 0;

    String deniedButton() default "";

    int deniedBtnResId() default 0;

    String settingText() default "";

    int settingResId() default 0;

    boolean needGotoSetting() default false;

    boolean runIgnorePermission() default false;
}
