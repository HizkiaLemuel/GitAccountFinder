package com.hizkialemuel.git_account_finder.module.search.view;

import com.hizkialemuel.git_account_finder.base.BaseView;
import com.hizkialemuel.git_account_finder.bean.response.HttpResponse;

import java.io.Serializable;

public interface SearchView extends BaseView {
    void handleResult(Serializable data, HttpResponse errorResponse);

}
