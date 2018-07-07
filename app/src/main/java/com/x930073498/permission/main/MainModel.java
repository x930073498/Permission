package com.x930073498.permission.main;


import android.annotation.SuppressLint;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.x930073498.baseitemlib.BaseItem;
import com.x930073498.baseitemlib.BaseItemWrapper;
import com.x930073498.permission.HostType;
import com.x930073498.IntentKey;
import com.x930073498.R;
import com.x930073498.permission.RequestManager;
import com.x930073498.Router;
import com.x930073498.permission.base.RxListener;
import com.x930073498.databinding.LayoutItemImageBinding;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainModel implements MainContract.IModel {
    @SuppressLint("CheckResult")
    @Override
    public void getGirls(int pageSize, int page, RxListener<List<BaseItem>> listener) {
        MainContract.IService service = RequestManager.getInstance().get(HostType.TYPE_GANK).create(MainContract.IService.class);
        service.getGirls("福利", pageSize, page).map(listGankResult -> BaseItemWrapper.toBaseItem(listGankResult.getResults(),
                item -> R.layout.layout_item_image,
                (baseAdapter, girlImage, viewDataBinding, i) -> {
                    LayoutItemImageBinding binding = (LayoutItemImageBinding) viewDataBinding;
                    binding.getRoot().setOnClickListener(v -> onItemClick(binding.image, girlImage.getUrl()));
                    Glide.with(binding.image).load(girlImage.getUrl()).apply(RequestOptions.noAnimation()).into(binding.image);
                }, null)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(listener::onSuccess, throwable -> listener.onError(throwable.getMessage()), listener::onComplete);

    }

    private void onItemClick(ImageView imageView, String url) {
        ARouter.getInstance().build(Router.PATH_PREVIEW)
                .withString(IntentKey.KEY_FIRST, url)
                .navigation(imageView.getContext());
    }
}
