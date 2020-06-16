package com.hizkialemuel.git_account_finder.http.services;

import com.hizkialemuel.git_account_finder.bean.model.User;
import com.hizkialemuel.git_account_finder.bean.response.HttpResponse;
import com.hizkialemuel.git_account_finder.bean.response.SearchResponse;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface GitSearchService {

    String PREFIX = "search/";

    @GET(PREFIX+"users")
    Observable<SearchResponse<User>> searchUsers(
            @Header("Cache-Control") String cacheControl,
            @QueryMap Map<String, Object> params);
}
