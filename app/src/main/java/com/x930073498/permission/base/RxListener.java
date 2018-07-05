package com.x930073498.permission.base;

public interface RxListener<T> {
    void onSuccess(T result);

    default void onError(String msg) {

    }

    default void onComplete() {

    }


}
