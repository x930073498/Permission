package com.x930073498.permission.main;

import com.lain.loadmorehelper.IDataSwapper;
import com.lain.loadmorehelper.LoadMoreHelper;
import com.lain.loadmorehelper.PageData;
import com.x930073498.baseitemlib.BaseItem;
import com.x930073498.permission.base.IBaseModel;
import com.x930073498.permission.base.IBasePresenter;
import com.x930073498.permission.base.IBaseView;
import com.x930073498.permission.base.RxListener;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MainContract {
    interface IService {
        @GET("api/data/{category}/{pageSize}/{page}")
        Observable<GankResult<List<GirlImage>>> getGirls(@Path(value = "category",encoded = true) String category,@Path("pageSize") int pageSize, @Path("page") int page);

    }

    interface IModel extends IBaseModel {
        void getGirls(int pageSize, int page, RxListener<List<BaseItem>> listener);
    }

    interface IView extends IBaseView, LoadMoreHelper.AsyncDataLoader<BaseItem>, IDataSwapper<BaseItem> {
        void setRecycler();

        void onLoadEnd(PageData<BaseItem> pageData);

    }

    interface IPresenter extends IBasePresenter<IModel, IView> {
        void getGirls(int page);

        void setViews();
    }
}
