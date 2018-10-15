package com.x930073498.permission;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.x930073498.R;
import com.x930073498.annotations.NeedPermission;
import com.x930073498.annotations.PermissionsDenied;
import com.x930073498.annotations.PermissionsGranted;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionCheckSDK.init(getApplication());
        setContentView(R.layout.activity_main);
    }

    @NeedPermission(permissions = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @PermissionsGranted(permission = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void onGranted() {
        Toast.makeText(getApplicationContext(), "允许", Toast.LENGTH_SHORT).show();
    }

    @PermissionsDenied(permission = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void onDenied() {
        Toast.makeText(getApplicationContext(), "拒绝", Toast.LENGTH_SHORT).show();
    }
}
