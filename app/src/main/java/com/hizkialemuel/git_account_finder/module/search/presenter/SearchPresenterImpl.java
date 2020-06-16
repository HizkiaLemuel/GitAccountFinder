package com.hizkialemuel.git_account_finder.module.search.presenter;

import android.text.TextUtils;

import com.hizkialemuel.git_account_finder.base.BasePresenterImpl;
import com.hizkialemuel.git_account_finder.bean.response.HttpResponse;
import com.hizkialemuel.git_account_finder.module.search.model.SearchInteractor;
import com.hizkialemuel.git_account_finder.module.search.model.SearchInteractorImpl;
import com.hizkialemuel.git_account_finder.module.search.view.SearchView;

import java.io.Serializable;

import rx.Subscription;


public class SearchPresenterImpl extends BasePresenterImpl< SearchView, Serializable> implements SearchPresenter {
    private SearchInteractor<Serializable> searchInteractor;

    public SearchPresenterImpl(SearchView view) {
        super(view);
        searchInteractor = new SearchInteractorImpl();
    }

    @Override
    public void beforeRequest() {
        super.beforeRequest();
    }

    @Override
    public void requestError(HttpResponse response) {
        if (response != null) {
            super.requestError(response);
            if (mView != null) {
                mView.handleResult(null, response);
            }
        }
    }

    @Override
    public void requestSuccess(Serializable data) {
        super.requestSuccess(data);
        if (mView != null) {
            mView.handleResult(data, null);
        }
    }

    @Override
    public void requestSearch(String query, int page) {
        if(TextUtils.isEmpty(query)) return;
        Subscription subscription = searchInteractor.requestSearch(this, query, page);
        compositeSubscription.add(subscription);
    }
}
