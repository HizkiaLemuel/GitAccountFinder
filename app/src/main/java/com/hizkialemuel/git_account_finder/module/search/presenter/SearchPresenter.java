package com.hizkialemuel.git_account_finder.module.search.presenter;

import com.hizkialemuel.git_account_finder.base.BasePresenter;

public interface SearchPresenter extends BasePresenter {
    void requestSearch(String query, int page);

}
