package com.hizkialemuel.git_account_finder.module.search.model;

import com.hizkialemuel.git_account_finder.callback.RequestCallback;

import rx.Subscription;

public interface SearchInteractor<T> {
    Subscription requestSearch(RequestCallback<T> callback, String query, int page);

}
