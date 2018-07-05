package com.x930073498.permission;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.x930073498.permission.HostType.TYPE_GANK;

@Retention(RetentionPolicy.SOURCE)
@IntDef({TYPE_GANK})
public @interface HostType {
    int TYPE_GANK = 1;
}
