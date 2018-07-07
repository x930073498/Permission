package com.x930073498.permission.main.preview;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.x930073498.R;
import com.x930073498.annotations.NeedPermission;
import com.x930073498.databinding.ActivityImagePreviewBinding;

import static com.x930073498.IntentKey.KEY_FIRST;
import static com.x930073498.Router.PATH_PREVIEW;


@Route(path = PATH_PREVIEW)
public class ImagePreviewActivity extends AppCompatActivity implements ImagePreviewContract.IView {
    private static final String TAG = "ImagePreviewActivity";
    private ActivityImagePreviewBinding binding;
    @Autowired(name = KEY_FIRST)
    String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_preview);
        binding.image.setBackgroundColor(Color.BLACK);
        ARouter.getInstance().inject(this);
        Glide.with(this).load(url).into(binding.image);
        binding.image.postDelayed(this::testData, 2000);
    }

    @NeedPermission(permissions = Manifest.permission.READ_PHONE_STATE, impl = ImagePreviewActivityProxy.class)
    public void testData() {
        Log.d(TAG, "testData: ");
    }
}
