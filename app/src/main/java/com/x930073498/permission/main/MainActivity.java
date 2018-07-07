package com.x930073498.permission.main;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.lain.loadmorehelper.LoadMoreHelper;
import com.lain.loadmorehelper.PageData;
import com.x930073498.annotations.NeedPermission;
import com.x930073498.annotations.PermissionsDenied;
import com.x930073498.annotations.PermissionsGranted;
import com.x930073498.baseitemlib.BaseAdapter;
import com.x930073498.baseitemlib.BaseItem;
import com.x930073498.R;
import com.x930073498.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.IView {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private LoadMoreHelper<BaseItem> helper;
    private BaseAdapter adapter;
    private MainContract.IPresenter presenter;

    @PermissionsDenied
    public void onDenied() {
        Log.d(TAG, "onDenied: ");
    }

    @PermissionsDenied(isAccurate = true)
    public void onTestDenied() {
        Log.d(TAG, "onTestDenied: ");
    }

    @PermissionsGranted
    public void onGranted() {
        Log.d(TAG, "onGranted: ");
    }
    @PermissionsGranted(permission = Manifest.permission.READ_PHONE_STATE)
    public void onStateGranted(){
        Log.d(TAG, "onStateGranted: ");
    }
    @PermissionsDenied(permission=Manifest.permission.READ_PHONE_STATE)
    public void onStateDenied(){
        Log.d(TAG, "onStateDenied: ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        presenter = new MainPresenter(this, new MainModel());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        presenter.setViews();
        binding.recycler.postDelayed(this::getData, 2000);
    }

    @NeedPermission(permissions = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void getData() {
        Log.d(TAG, "getData: enter this line");
        testData();
    }

    @NeedPermission(permissions = Manifest.permission.READ_PHONE_STATE)
    public void testData() {
        Log.d(TAG, "testData: ");
    }

    @Override
    public void setRecycler() {
        adapter = new BaseAdapter();
        binding.recycler.setAdapter(adapter);
        binding.recycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.recycler.setItemAnimator(new DefaultItemAnimator());
        helper = LoadMoreHelper.create(binding.refresh).setDataSwapper(this).setAsyncDataLoader(this).build();
        helper.startPullData(true);
    }

    @Override
    public void onLoadEnd(PageData<BaseItem> pageData) {
        helper.onLoadEnd(pageData);
    }


    @Override
    public void swapData(List<? extends BaseItem> list) {
        adapter.setData(list);
    }

    @Override
    public void appendData(List<? extends BaseItem> list) {
        List<BaseItem> data = (List<BaseItem>) adapter.getData();
        data.addAll(list);
        adapter.setData(data);
    }


    @Override
    public void startLoadData(int page, @Nullable PageData<BaseItem> lastPageData) {
        presenter.getGirls(page);
    }
}
