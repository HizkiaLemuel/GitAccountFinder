package com.hizkialemuel.git_account_finder.http;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

public class HostType {
    public static final int TYPE_COUNT = 2;

    @HostTypeChecker
    public static final int COMMON_API = 1;
    @HostTypeChecker
    public static final int TEST_API = 2;


    @IntDef({COMMON_API, TEST_API})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HostTypeChecker {
    }
}
