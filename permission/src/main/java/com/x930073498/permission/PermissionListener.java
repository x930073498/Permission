package com.x930073498.permission;

/**
 * permissionlistener
 * <br>
 *
 * @author simon
 * @since 2016-05-15
 */
public interface PermissionListener {
    void permissionGranted(String[] permissions,int[] requestCodes);

    void permissionDenied(String[] permissions,int[] requestCodes);
}
