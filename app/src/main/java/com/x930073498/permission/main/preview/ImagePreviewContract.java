package com.x930073498.permission.main.preview;

import com.x930073498.permission.base.IBaseModel;
import com.x930073498.permission.base.IBasePresenter;
import com.x930073498.permission.base.IBaseView;

public interface ImagePreviewContract {
    interface IModel extends IBaseModel{}
    interface IView extends IBaseView{}
    interface IPresenter extends IBasePresenter<IModel,IView>{}
}
