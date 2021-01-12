package com.arch.base.core.model;

public interface MvvmNetworkObserver<F> {
    void onSuccess(F t, boolean isFromCache);
    void onFailure(Throwable e);
}
