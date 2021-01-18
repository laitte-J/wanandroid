package com.arch.base.core.model;

public interface IBaseModelListener<T> {
    void onLoadFinish(MvvmBaseModel model, T data, PagingResult... pageResult);

    void onLoadFail(MvvmBaseModel model, Throwable prompt,PagingResult... pageResult);

}