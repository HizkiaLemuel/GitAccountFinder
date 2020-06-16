package com.hizkialemuel.git_account_finder.callback;

import com.hizkialemuel.git_account_finder.bean.response.HttpResponse;

public interface RequestCallback<T> {
    void beforeRequest();

    void requestError(HttpResponse response);

    void requestComplete();

    void requestSuccess(T data);
}
