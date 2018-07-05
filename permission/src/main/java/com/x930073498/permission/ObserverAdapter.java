package com.x930073498.permission;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

public class ObserverAdapter implements LifecycleObserver {
    private static final String TAG = "ObserverAdapter";

    @OnLifecycleEvent( Lifecycle.Event.ON_CREATE)
    public void onCreate(){
        Log.d(TAG, "onCreate: ");
    }
}
