package com.emcrp.network.observer;

import com.arch.base.core.model.MvvmBaseModel;
import com.arch.base.core.model.MvvmNetworkObserver;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


public class BaseObserver<T> implements Observer<T> {
    MvvmBaseModel baseModel;
    MvvmNetworkObserver<T> mvvmNetworkObserver;

    public BaseObserver(MvvmBaseModel baseModel, MvvmNetworkObserver<T> mvvmNetworkObserver) {
        this.baseModel = baseModel;
        this.mvvmNetworkObserver = mvvmNetworkObserver;
    }

    @Override
    public void onError(Throwable e) {
        mvvmNetworkObserver.onFailure(e);
    }

    @Override
    public void onNext(T t) {
        mvvmNetworkObserver.onSuccess(t, false);
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (baseModel != null) {
            baseModel.addDisposable(d);
        }
    }

    @Override
    public void onComplete() {
    }
}
