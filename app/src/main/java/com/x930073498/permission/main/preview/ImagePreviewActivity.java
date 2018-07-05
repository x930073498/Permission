package com.x930073498.permission.main.preview;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.x930073498.permission.R;
import com.x930073498.permission.databinding.ActivityImagePreviewBinding;

import static com.x930073498.permission.IntentKey.KEY_FIRST;
import static com.x930073498.permission.Router.PATH_PREVIEW;


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
    }
}
