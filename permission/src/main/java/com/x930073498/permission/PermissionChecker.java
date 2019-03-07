package com.x930073498.permission;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by x930073498 on 2019/1/24 0024.
 */
public class PermissionChecker {
    private static String TAG = String.valueOf(System.currentTimeMillis());

    private final FragmentManager manager;


    public static PermissionChecker attach(FragmentActivity activity) {
        return new PermissionChecker(activity);
    }

    public static PermissionChecker attach(Fragment fragment) {
        return new PermissionChecker(fragment);
    }

    public static PermissionChecker attach(FragmentManager manager) {
        return new PermissionChecker(manager);
    }

    public static PermissionChecker guess() {
        if (list == null || list.isEmpty()) throw new RuntimeException("没有可以用作请求的权限的Activity");
        for (Activity activity :
                list) {
            if (activity instanceof FragmentActivity)
                return new PermissionChecker((FragmentActivity) activity);
        }
        throw new RuntimeException("当前使用的activity中没有FragmentActivity");
    }

    private PermissionChecker(FragmentActivity activity) {
        manager = activity.getSupportFragmentManager();
    }

    private PermissionChecker(Fragment fragment) {
        manager = fragment.getChildFragmentManager();
    }

    private PermissionChecker(FragmentManager manager) {
        this.manager = manager;
    }

    private PermissionFragment getPermissionFragment() {
        Fragment fragment = manager.findFragmentByTag(TAG);
        PermissionFragment permissionFragment;
        if (fragment == null) {
            permissionFragment = new PermissionFragment();
            manager.beginTransaction().add(permissionFragment, TAG).commitNow();
        } else {
            if (fragment instanceof PermissionFragment) {
                permissionFragment = (PermissionFragment) fragment;
            } else {
                TAG = String.valueOf(System.currentTimeMillis());
                permissionFragment = new PermissionFragment();
                manager.beginTransaction().add(permissionFragment, TAG).commitNow();
            }
        }
        return permissionFragment;
    }

    public Action request(String... permissions) {
        return getPermissionFragment().request(permissions);
    }

    private static boolean isGranted(Activity activity, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        } else return true;
    }

    private static boolean isRevoked(Activity activity, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return activity.getPackageManager().isPermissionRevokedByPolicy(permission, activity.getPackageName());
        }
        return false;
    }

    public boolean isGranted(String permission) {
        return getPermissionFragment().isGranted(permission);
    }

    private boolean isRevoked(String permission) {
        return getPermissionFragment().isRevoked(permission);
    }


    static boolean shouldShowRequestPermissionRationaleImplementation(final Activity activity, final String... permissions) {
        if (activity == null) return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String p : permissions) {
                if (!isGranted(activity, p) && !activity.shouldShowRequestPermissionRationale(p)) {
                    return false;
                }
            }
        }
        return true;
    }


    public static void init(Application app) {
        if (!hasInit) {
            app.registerActivityLifecycleCallbacks(new LifecycleCallback());
            hasInit = true;
        }
    }

    private static boolean hasInit = false;

    private static final List<Activity> list = new ArrayList<>();

    private static Activity getTopActivity() {
        if (list.isEmpty()) return null;
        return list.get(list.size() - 1);
    }


    private static class LifecycleCallback implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            list.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            list.remove(activity);
        }
    }

}
