package com.hizkialemuel.git_account_finder.base;

import android.text.TextUtils;

import com.hizkialemuel.git_account_finder.bean.response.HttpResponse;
import com.hizkialemuel.git_account_finder.callback.RequestCallback;
import com.hizkialemuel.git_account_finder.http.RetrofitException;
import com.socks.library.KLog;

import androidx.annotation.CallSuper;
import retrofit2.HttpException;
import rx.Subscriber;

public class BaseSubscriber<T> extends Subscriber<T> {
    private RequestCallback<T> mRequestCallback;

    public BaseSubscriber(RequestCallback<T> requestCallback) {
        mRequestCallback = requestCallback;
    }

    @CallSuper
    @Override
    public void onStart() {
        KLog.v("BaseSubscriber onStart");
        super.onStart();
        if (mRequestCallback != null) {
            mRequestCallback.beforeRequest();
        }
    }

    @CallSuper
    @Override
    public void onCompleted() {
        KLog.v("BaseSubscriber onCompleted");
        if (mRequestCallback != null) {
            mRequestCallback.requestComplete();
        }
    }

    @Override
    public void onError(Throwable e) {
        KLog.v("BaseSubscriber", "HPtes onError: " + e.getMessage());
        e.printStackTrace();
        try {
            if (mRequestCallback != null) {
                try {
                    if (e instanceof HttpException) {
                        KLog.v("BaseSubscriber", "onError: HPtes Http Exception");
                        HttpException httpResponse = (HttpException) e;
                        HttpResponse response = new HttpResponse();
                        response.setMeta(httpResponse);
                        response.setCode(((HttpException) e).code());
                        switch (response.getCode()) {
                            case 422:
                            case 404:
                                response.setMessage("Something wrong");
                                break;
                            case 403:
                                response.setMessage("To much request, Please wait for a minute");
                                break;
                            default:
                                response.setMessage(((HttpException) e).message());

                        }

                        mRequestCallback.requestError(response);
                    } else {
                        RetrofitException error = (RetrofitException) e;
                        HttpResponse response;
                        if (!TextUtils.isEmpty(error.getMessage()) && error.getMessage().equalsIgnoreCase("timeout")) {
                            response = new HttpResponse();
                            response.setMeta(error);
                            response.setCode(((HttpException) e).code());
                            response.setMessage("Network Error");
                        } else {
                            response = (error.getErrorBodyAs(HttpResponse.class));
                            if (response != null) {
                                KLog.v("onError response : [" + response.getMeta());
                            }
                        }

                        mRequestCallback.requestError(response);
                    }
                } catch (Exception e1) {

                }
            }
        } catch (Exception e2) {

        }
    }

    @Override
    public void onNext(T t) {
        if (mRequestCallback != null) {
            mRequestCallback.requestSuccess(t);
        }
    }
}
