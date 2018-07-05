package com.x930073498.permission;

public @interface AfterGranted {
    String[] permissions() default "";
}
