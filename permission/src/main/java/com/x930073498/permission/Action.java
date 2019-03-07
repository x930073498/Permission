package com.x930073498.permission;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Action {
    private boolean isRequest = false;
    private final int requestCode;
    private final String[] permissions;
    private final PermissionFragment fragment;
    private SingleCallback singleCallback;
    private ListCallback listCallback;

    private int singleIndex = 0;


    boolean isRequest() {
        return isRequest;
    }

    Action(int requestCode, PermissionFragment fragment, String... permission) {
        this.requestCode = requestCode;
        this.fragment = fragment;
        this.permissions = permission;
    }

    public void forEach(SingleCallback callback) {
        if (listCallback != null) return;
        this.singleCallback = callback;
        if (callback == null) return;
        fragment.push(this);
    }

    public void all(ListCallback callback) {
        if (singleCallback != null) return;
        this.listCallback = callback;
        if (callback == null) return;
        fragment.push(this);
    }


    void onRequestPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults) {
        isRequest = false;
        if (singleCallback != null) {
            if (permissions.length > 0 && grantResults.length > 0) {
                String name = permissions[0];
                Permission permission = new Permission(name, singleIndex - 1,this.permissions.length, PackageManager.PERMISSION_GRANTED == grantResults[0], PermissionChecker.shouldShowRequestPermissionRationaleImplementation(fragment.getActivity(), name));
                if (singleCallback.callback(permission)) {
                    request();
                } else {
                    singleIndex = 0;
                    fragment.onComplete();
                }
            }
            return;
        }
        if (listCallback != null) {
            if (permissions.length == grantResults.length) {
                List<Permission> permissionList = new ArrayList<>();
                for (int index = 0; index < permissions.length; index++
                ) {
                    permissionList.add(new Permission(permissions[index], index, this.permissions.length,PackageManager.PERMISSION_GRANTED == grantResults[index], PermissionChecker.shouldShowRequestPermissionRationaleImplementation(fragment.getActivity(), permissions[index])));
                }
                listCallback.callback(permissionList);
                fragment.onComplete();
            }
        }


    }


    void onAttach() {
        if (isRequest) return;
        if (listCallback == null && singleCallback == null) return;
        request();
    }

    void request() {
        isRequest = true;
        if (permissions.length == 0) return;
        if (singleCallback != null) {
            if (singleIndex >= permissions.length) {
                fragment.onComplete();
                return;
            }
            int index = singleIndex;
            singleIndex++;
            if (fragment.isGranted(permissions[index])) {
                onRequestPermissionsResult(new String[]{permissions[index]}, new int[]{PackageManager.PERMISSION_GRANTED});
                return;
            }
            fragment.requestPermissions(new String[]{permissions[index]}, requestCode);
            return;
        }
        if (listCallback != null) {
            fragment.requestPermissions(permissions, requestCode);
        }
    }

}