package com.hizkialemuel.git_account_finder.module.search.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hizkialemuel.git_account_finder.base.BaseSubscriber;
import com.hizkialemuel.git_account_finder.bean.model.User;
import com.hizkialemuel.git_account_finder.bean.response.SearchResponse;
import com.hizkialemuel.git_account_finder.callback.RequestCallback;
import com.hizkialemuel.git_account_finder.http.Api;
import com.hizkialemuel.git_account_finder.http.HostType;
import com.hizkialemuel.git_account_finder.http.manager.RetrofitManager;
import com.hizkialemuel.git_account_finder.util.JsonUtil;
import com.hizkialemuel.git_account_finder.util.RxUtil;
import com.socks.library.KLog;

import java.lang.reflect.Type;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

public class SearchInteractorImpl implements SearchInteractor {
    @Override
    public Subscription requestSearch(RequestCallback callback, String query, int page) {
        return RetrofitManager.getInstance(HostType.COMMON_API).requestSearch(query, page)
                .throttleLast(Api.MAX_REQUEST_COUNT,Api.MAX_REQUEST_TIMEUNIT)
                .subscribe(new BaseSubscriber<>(callback));
    }
}