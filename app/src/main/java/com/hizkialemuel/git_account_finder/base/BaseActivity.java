package com.hizkialemuel.git_account_finder.base;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.hizkialemuel.git_account_finder.R;
import com.hizkialemuel.git_account_finder.module.search.ui.SearchActivity;
import com.hizkialemuel.git_account_finder.util.RxBus;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView, View.OnClickListener {

    protected T presenter;
    protected View view;
    private boolean mBack2Flag = false;

    private Observable<Boolean> finishObservable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initFinishRxBus();

        initView();
    }

    protected abstract void initView();

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (finishObservable != null) {
            RxBus.get().unregister(RxBus.KEY_APP_FINISH, finishObservable);
        }
        presenter.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null) presenter.onResume();
    }

    @Override
    public void toast(String msg) {
        if (!isFinishing()) {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (this instanceof SearchActivity) {
                if (!mBack2Flag && !isFinishing()) {
                    Toast.makeText(this, getResources().getString(R.string.common_exit_msg), Toast.LENGTH_SHORT).show();
                    mBack2Flag = true;
                    Observable.timer(2000, TimeUnit.MILLISECONDS)
                            .subscribe(new Action1<Object>() {
                                @Override
                                public void call(Object o) {
                                    mBack2Flag = false;
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                }
                            }, new Action0() {
                                @Override
                                public void call() {
                                }
                            });
                    return false;
                } else {
                    RxBus.get().post(RxBus.KEY_APP_FINISH, true);
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    private void initFinishRxBus() {
        finishObservable = RxBus.get().register(RxBus.KEY_APP_FINISH, Boolean.class);
        finishObservable.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean isBackButton) {
                try {
                    if (isBackButton) {
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
            }
        }, new Action0() {
            @Override
            public void call() {
            }
        });
    }
}
