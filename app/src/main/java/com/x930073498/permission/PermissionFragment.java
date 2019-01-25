package com.x930073498.permission;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.ArrayMap;
import android.view.View;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by x930073498 on 2019/1/24 0024.
 */
public final class PermissionFragment extends Fragment {
    private int requestCode = 1;
    private ArrayMap<Integer, Action> requests = new ArrayMap<>();
    private ConcurrentLinkedQueue<Action> queue = new ConcurrentLinkedQueue<>();
    private boolean isAttached = false;

    private int getRequestCode() {
        requestCode++;
        if (requestCode >= 60000) return 1;
        else return requestCode;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        isAttached = true;
        request();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Action action = requests.get(requestCode);
        if (action == null) return;
        action.onRequestPermissionsResult(permissions, grantResults);
    }

    Action request(String... permission) {
        int requestCode = getRequestCode();
        Action action = new Action(requestCode, this, permission);
        requests.put(requestCode, action);
        return action;
    }

    void push(Action action) {
        queue.offer(action);
        request();
    }

    void onComplete() {
        queue.poll();
        request();
    }

    private synchronized void request() {
        if (!isAttached) return;
        if (queue.isEmpty()) return;
        Action action = queue.peek();
        while (action == null && !queue.isEmpty()) {
            queue.poll();
            action = queue.peek();
        }
        if (action == null) return;
        if (action.isRequest()) return;
        action.request();
    }


    boolean isGranted(String permission) {
        final FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity == null) {
            throw new IllegalStateException("This fragment must be attached to an activity.");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return fragmentActivity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }


    boolean isRevoked(String permission) {
        final FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity == null) {
            throw new IllegalStateException("This fragment must be attached to an activity.");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return fragmentActivity.getPackageManager().isPermissionRevokedByPolicy(permission, getActivity().getPackageName());
        }
        return false;
    }


}
