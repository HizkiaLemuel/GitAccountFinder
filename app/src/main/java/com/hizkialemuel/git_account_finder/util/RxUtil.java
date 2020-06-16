package com.hizkialemuel.git_account_finder.util;

import com.hizkialemuel.git_account_finder.bean.response.HttpResponse;
import com.hizkialemuel.git_account_finder.http.ApiException;
import com.socks.library.KLog;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class RxUtil {
    public static <T> Observable.Transformer<HttpResponse<T>, T> handleResult() {
        return httpResponseObservable -> httpResponseObservable.flatMap(new Func1<HttpResponse<T>, Observable<T>>() {
            @Override
            public Observable<T> call(HttpResponse<T> tHttpResponse) {
                if (tHttpResponse.getMeta() instanceof ApiException) {
                    KLog.v("RxUtil","call: HPtes error api");
                    return Observable.error((ApiException)tHttpResponse.getMeta());
                } else {
                    return createData(tHttpResponse.getMeta());
                }
            }
        });
    }

    public static <T> Observable<T> createData(final T t) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}

