package com.x930073498.permission;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.x930073498.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionCheckSDK.init(getApplication());
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    //    @PermissionsGranted(permission = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void onGranted() {
        Toast.makeText(getApplicationContext(), "允许", Toast.LENGTH_SHORT).show();
    }

//    @PermissionsDenied(permission = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void onDenied() {
        Log.e(TAG, "onDenied: " );
    }
//    @NeedPermission(permissions = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void permissionCheck(View view) {
    PermissionChecker.attach(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA).forEach(permission -> {
        Log.e(TAG, "permissionCheck: "+permission );
        return true;
    });

        PermissionChecker.guess().request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                .all(result -> Log.e(TAG, "permissionCheck: list="+result ));
        Log.e(TAG, "permissionCheck: " );
    }
}
