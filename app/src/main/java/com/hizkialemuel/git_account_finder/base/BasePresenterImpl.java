package com.hizkialemuel.git_account_finder.base;

import com.hizkialemuel.git_account_finder.bean.response.HttpResponse;
import com.hizkialemuel.git_account_finder.callback.RequestCallback;
import com.socks.library.KLog;

import rx.subscriptions.CompositeSubscription;

public class BasePresenterImpl<T extends BaseView, V> implements BasePresenter, RequestCallback<V> {
    protected CompositeSubscription compositeSubscription = new CompositeSubscription();
    protected T mView;

    public BasePresenterImpl(T view) {
        mView = view;
    }

    @Override
    public void onDestroy() {
        if (compositeSubscription != null) {
            compositeSubscription.clear();
        }
    }

    @Override
    public void onResume() {

    }

    @Override
    public void beforeRequest() {
        KLog.v("BasePresenterImpl","beforeRequest: HPtes");
        if (mView != null) {
            mView.showProgress();
        }
    }

    @Override
    public void requestError(HttpResponse response) {
        KLog.v("BasePresenterImpl", "requestError: ");
        if (mView != null) {
            mView.hideProgress();
        }
    }

    @Override
    public void requestComplete() {
        if (mView != null) {
            mView.hideProgress();
        }
    }

    @Override
    public void requestSuccess(V data) {
        if (mView != null) {
            mView.hideProgress();
        }
    }
}
