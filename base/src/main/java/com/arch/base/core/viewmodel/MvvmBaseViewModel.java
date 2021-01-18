package com.arch.base.core.viewmodel;

import android.util.Log;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.arch.base.core.model.ExceptionHandle;
import com.arch.base.core.model.IBaseModelListener;
import com.arch.base.core.model.MvvmBaseModel;
import com.arch.base.core.model.PagingResult;
import com.arch.base.core.model.ResponeThrowable;

import java.util.List;


public abstract class MvvmBaseViewModel<M extends MvvmBaseModel, D> extends ViewModel implements LifecycleObserver, IBaseModelListener<List<D>> {
    protected M model;
    public MutableLiveData<ObservableList<D>> dataList = new MutableLiveData<>();
    public MutableLiveData<ViewStatus> viewStatusLiveData = new MutableLiveData<>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public static final int FAILED_NOT_LOGIN = -1001; //请先登录

    public MvvmBaseViewModel() {
        dataList.setValue(new ObservableArrayList<>());
        viewStatusLiveData.setValue(ViewStatus.LOADING);
        errorMessage.setValue("");
    }

    public void tryToRefresh() {
        if (model != null) {
            model.refresh();
        }
    }

    @Override
    protected void onCleared() {
        if (model != null) {
            model.cancel();
        }
    }

    @Override
    public void onLoadFinish(MvvmBaseModel model, List<D> data, PagingResult... pagingResult) {
        if (model == this.model) {
            if (model.isPaging()) {
                if (pagingResult[0].isFirstPage) {
                    dataList.getValue().clear();
                }
                if (pagingResult[0].isEmpty) {
                    if (pagingResult[0].isFirstPage) {
                        viewStatusLiveData.setValue(ViewStatus.EMPTY);
                    } else {
                        viewStatusLiveData.setValue(ViewStatus.NO_MORE_DATA);
                    }
                } else {
                    dataList.getValue().addAll(data);
                    viewStatusLiveData.setValue(ViewStatus.SHOW_CONTENT);
                }
            } else {
                dataList.getValue().clear();
                dataList.getValue().addAll(data);
                viewStatusLiveData.setValue(ViewStatus.SHOW_CONTENT);
            }
            viewStatusLiveData.postValue(viewStatusLiveData.getValue());
            dataList.postValue(dataList.getValue());
        }
    }

    @Override
    public void onLoadFail(MvvmBaseModel model, Throwable e, PagingResult... pagingResult) {
        if (e instanceof ResponeThrowable) {
            ResponeThrowable responeThrowable = (ResponeThrowable) e;
            if (model.isPaging() && !pagingResult[0].isFirstPage) {
                viewStatusLiveData.setValue(ViewStatus.LOAD_MORE_FAILED);
            } else {
                if (responeThrowable.code == FAILED_NOT_LOGIN) {
                    viewStatusLiveData.setValue(ViewStatus.TOKEN_ERROR);
                } else {
                    viewStatusLiveData.setValue(ViewStatus.REFRESH_ERROR);
                }
            }
            errorMessage.setValue(responeThrowable.message);

        } else {
            if (model.isPaging() && !pagingResult[0].isFirstPage) {
                viewStatusLiveData.setValue(ViewStatus.LOAD_MORE_FAILED);
            } else {
                viewStatusLiveData.setValue(ViewStatus.REFRESH_ERROR);
            }
            errorMessage.setValue(ExceptionHandle.handleException(e).message);
        }
        viewStatusLiveData.postValue(viewStatusLiveData.getValue());
        Log.e(" mvvmbasevm  onLoadFail", "onLoadFail msg==========: " + viewStatusLiveData.getValue().name());
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestory() {
        onCleared();
        model.unRegister(this);
    }

//    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
//    private void onResume() {
//        dataList.postValue(dataList.getValue());
//        viewStatusLiveData.postValue(viewStatusLiveData.getValue());
//    }
}
