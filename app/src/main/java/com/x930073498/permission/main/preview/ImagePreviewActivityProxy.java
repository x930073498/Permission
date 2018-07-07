package com.x930073498.permission.main.preview;

import android.util.Log;

import com.x930073498.permission.PermissionsProxy;

public class ImagePreviewActivityProxy implements PermissionsProxy<ImagePreviewActivity> {
    private static final String TAG = "ImagePreviewActivityProxy";

    @Override
    public void denied(ImagePreviewActivity target, String[] permission, int[] code) {
        Log.d(TAG, "denied: ");
    }

    @Override
    public void granted(ImagePreviewActivity target, String[] permission, int[] code) {
        Log.d(TAG, "granted: ");
    }
}
