package com.x930073498.permission;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.x930073498.annotations.*;
import com.x930073498.R;

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
}
