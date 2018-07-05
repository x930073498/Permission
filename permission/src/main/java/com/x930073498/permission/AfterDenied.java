package com.x930073498.permission;

public @interface AfterDenied {
    String[] permissions() default "";
}
