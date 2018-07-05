package com.x930073498.permission.main;

import com.lain.loadmorehelper.PageData;

public class MainPresenter implements MainContract.IPresenter {
    private MainContract.IView mView;
    private MainContract.IModel mModel;

    public MainPresenter(MainContract.IView mView, MainContract.IModel mModel) {
        this.mView = mView;
        this.mModel = mModel;
    }

    @Override
    public void getGirls(int page) {
        mModel.getGirls(40, page, result -> mView.onLoadEnd(PageData.createSuccess(page, result, result.size() == 40)));
    }

    @Override
    public void setViews() {
        mView.setRecycler();
    }
}
