package com.x930073498.annotations;

import com.x930073498.permission.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface PermissionsGranted {
    String[] permission() default Constants.ALL;

    int[] requestCode() default 0;

    boolean isAccurate() default false;

}
