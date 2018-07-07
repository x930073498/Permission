package com.x930073498.permission;

public interface PermissionListener {
    void permissionGranted(String[] permissions,int[] requestCodes);

    void permissionDenied(String[] permissions,int[] requestCodes);
}
