package com.x930073498.permission;

/**
 * permissionlistener
 * <br>
 *
 * @author simon
 * @since 2016-05-15
 */
public interface PermissionListener {
    void permissionGranted(String[] permissions);

    void permissionDenied(String[] permissions);
}
