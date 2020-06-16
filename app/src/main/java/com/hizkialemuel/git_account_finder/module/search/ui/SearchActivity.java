package com.hizkialemuel.git_account_finder.module.search.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.hizkialemuel.git_account_finder.R;
import com.hizkialemuel.git_account_finder.base.BaseActivity;
import com.hizkialemuel.git_account_finder.base.BaseRecyclerAdapter;
import com.hizkialemuel.git_account_finder.base.BaseRecyclerViewHolder;
import com.hizkialemuel.git_account_finder.bean.model.User;
import com.hizkialemuel.git_account_finder.bean.response.HttpResponse;
import com.hizkialemuel.git_account_finder.bean.response.SearchResponse;
import com.hizkialemuel.git_account_finder.databinding.ActivitySearchBinding;
import com.hizkialemuel.git_account_finder.module.search.presenter.SearchPresenter;
import com.hizkialemuel.git_account_finder.module.search.presenter.SearchPresenterImpl;
import com.hizkialemuel.git_account_finder.module.search.view.SearchView;
import com.hizkialemuel.git_account_finder.widget.LazyLoadRecyclerViewScrollListener;
import com.socks.library.KLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchView {

    private ActivitySearchBinding binding;
    private BaseRecyclerAdapter<User> adapter;
    private Subscription timer;
    private LinearLayoutManager layoutListManager;
    private LazyLoadRecyclerViewScrollListener lazyLoadRecyclerViewScrollListener;
    private String query = "";
    private boolean onLoadMore = false;
    private int total_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        presenter = new SearchPresenterImpl(this);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        timer = new Subscription() {
            @Override
            public void unsubscribe() {
            }

            @Override
            public boolean isUnsubscribed() {
                return false;
            }
        };

        layoutListManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutListManager.setItemPrefetchEnabled(false);

        initListener();

        updateData(new ArrayList<>(), false);
    }

    @Override
    public void handleResult(Serializable data, HttpResponse errorResponse) {
        if (data != null) {
            SearchResponse<User> searchData = (SearchResponse<User>) data;
            List<User> users = searchData.getItems();
            total_count = searchData.getTotalCount();
            if (onLoadMore) {
                updateData(users, true);
            } else {
                updateData(users, false);
            }
        } else if (errorResponse != null) {
            toast("Oops, "+errorResponse.getMessage());
            KLog.v("SearchActivity","handleResult: HPtes errorhandle "+errorResponse.getCode());
        }
    }

    @Override
    public void showProgress() {
        super.showProgress();
        if (!onLoadMore) {
            if (binding.vPlaceholder.sfShimmerPlaceholder.getVisibility() != View.VISIBLE) {
                binding.vPlaceholder.sfShimmerPlaceholder.setVisibility(View.VISIBLE);
                binding.vPlaceholder.sfShimmerPlaceholder.startShimmerAnimation();
                binding.vEmptyPlaceholder.getRoot().setVisibility(View.GONE);
                binding.rvUser.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void hideProgress() {
        super.hideProgress();
            binding.vPlaceholder.sfShimmerPlaceholder.setVisibility(View.GONE);
            binding.vEmptyPlaceholder.getRoot().setVisibility(View.GONE);
            binding.vPlaceholder.sfShimmerPlaceholder.stopShimmerAnimation();
            binding.rvUser.setVisibility(View.VISIBLE);
    }

    private void initListener() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    hideProgress();
                }else {
                    showProgress();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                onLoadMore = false;
                query = s.toString();
                timer.unsubscribe();
                timer = Observable.timer(500, TimeUnit.MILLISECONDS)
                        .subscribe((Action1<Object>) o -> {
                            if (!TextUtils.isEmpty(s.toString())) {
                                lazyLoadRecyclerViewScrollListener.resetState();
                                presenter.requestSearch(s.toString(), 1);
                                KLog.v("SearchActivity", "call: HPtes Call API " + s);
                            } else {
                                adapter.deleteAll();
                                hideProgress();
                            }
                        }, throwable -> {
                        }, () -> {
                        });
            }
        });

        lazyLoadRecyclerViewScrollListener = new LazyLoadRecyclerViewScrollListener(layoutListManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.requestSearch(query, page);
                KLog.v("SearchActivity", "onLoadMore: HPtes " + page);
                if (page != 1) onLoadMore = true;
            }
        };

    }

    private void initRecyclerView(List<User> data) {
        adapter = new BaseRecyclerAdapter<User>(this, data, layoutListManager) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.item_git_account_layout;
            }

            @Override
            public void bindData(BaseRecyclerViewHolder holder, int position, User user) {
                if (getItemViewType(position) == -1) return;
                holder.setText(R.id.tvUser, user.getLogin());
                holder.setImageUrl(R.id.ivUser, user.getAvatarUrl(), R.drawable.users);
            }
        };
        adapter.setHasStableIds(true);
        binding.rvUser.setLayoutManager(layoutListManager);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(getDrawable(R.drawable.divider));
        binding.rvUser.addItemDecoration(divider);
        binding.rvUser.setItemAnimator(new DefaultItemAnimator());
        if (binding.rvUser.getItemAnimator() != null) {
            binding.rvUser.getItemAnimator().setAddDuration(250);
            binding.rvUser.getItemAnimator().setMoveDuration(250);
            binding.rvUser.getItemAnimator().setChangeDuration(250);
            binding.rvUser.getItemAnimator().setRemoveDuration(250);
        }
        binding.rvUser.setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.rvUser.setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.rvUser.setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.rvUser.setItemViewCacheSize(30);
        binding.rvUser.addOnScrollListener(lazyLoadRecyclerViewScrollListener);
        binding.rvUser.setAdapter(adapter);

    }

    private void updateData(List<User> data, boolean isMoreData) {
        if (adapter == null) {
            initRecyclerView(data);
        } else {
            if (data.size() == 0 && total_count == 0) {
                setEmptyPlaceHolder(true);
            } else {
                setEmptyPlaceHolder(false);
                if (isMoreData) {
                    adapter.addMoreData(data);
                    KLog.v("SearchActivity", "HPtes: add More Data");

                }
                else {
                    adapter.setData(data);
                    KLog.v("SearchActivity", "HPtes: Set Data ");

                }
            }
        }
    }

    private void setEmptyPlaceHolder(boolean show) {
        binding.rvUser.setVisibility(show ? View.GONE : View.VISIBLE);
        binding.vEmptyPlaceholder.getRoot().setVisibility(show ? View.VISIBLE : View.GONE);
    }
}