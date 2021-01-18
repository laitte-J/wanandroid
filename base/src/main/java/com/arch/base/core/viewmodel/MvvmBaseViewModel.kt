package com.arch.base.core.viewmodel

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.*
import com.arch.base.core.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*

abstract class MvvmBaseViewModel<M : MvvmBaseModel<*, *>, D> : ViewModel(), LifecycleObserver,
    IBaseModelListener<List<D>>, CoroutineScope by MainScope() {
    lateinit var model: M

    @JvmField
    var dataList = MutableLiveData<ObservableList<D>>()

    @JvmField
    var viewStatusLiveData = MutableLiveData<ViewStatus?>()

    @JvmField
    var errorMessage = MutableLiveData<String>()
    fun tryToRefresh() {
        if (model != null) {
            model!!.refresh()
        }
    }

    override fun onCleared() {
        if (model != null) {
            model?.cancel()
        }
    }

    override fun onLoadFinish(
        model: MvvmBaseModel<*, out ArrayList<*>>,
        data: List<D>,
        vararg pageResult: PagingResult
    ) {
        launch {
            if (model === this@MvvmBaseViewModel.model) {
                if (model.isPaging) {
                    if (pageResult[0].isFirstPage) {
                        dataList.value!!.clear()
                    }
                    if (pageResult[0].isEmpty) {
                        if (pageResult[0].isFirstPage) {
                            viewStatusLiveData.setValue(ViewStatus.EMPTY)
                        } else {
                            viewStatusLiveData.setValue(ViewStatus.NO_MORE_DATA)
                        }
                    } else {
                        dataList.value!!.addAll(data!!)
                        viewStatusLiveData.setValue(ViewStatus.SHOW_CONTENT)
                    }
                } else {
                    dataList.value!!.clear()
                    dataList.value!!.addAll(data!!)
                    viewStatusLiveData.setValue(ViewStatus.SHOW_CONTENT)
                }
                viewStatusLiveData.postValue(viewStatusLiveData.value)
                dataList.postValue(dataList.value)
            }
        }
    }
//    override fun onLoadFinish(
//        model: MvvmBaseModel<*, *>,
//        data: List<D>,
//        vararg pagingResult: PagingResult
//    ) {
//
//    }

    override fun onLoadFail(
        model: MvvmBaseModel<*, *>,
        e: Throwable,
        vararg pagingResult: PagingResult
    ) {
        if (e is ResponeThrowable) {
            val responeThrowable: ResponeThrowable = e
            if (model.isPaging && !pagingResult[0].isFirstPage) {
                viewStatusLiveData.setValue(ViewStatus.LOAD_MORE_FAILED)
            } else {
                if (responeThrowable.code == FAILED_NOT_LOGIN) {
                    viewStatusLiveData.setValue(ViewStatus.TOKEN_ERROR)
                } else {
                    viewStatusLiveData.setValue(ViewStatus.REFRESH_ERROR)
                }
            }
            errorMessage.setValue(responeThrowable.message)
        } else {
            if (model.isPaging && !pagingResult[0].isFirstPage) {
                viewStatusLiveData.setValue(ViewStatus.LOAD_MORE_FAILED)
            } else {
                viewStatusLiveData.setValue(ViewStatus.REFRESH_ERROR)
            }
            errorMessage.setValue(ExceptionHandle.handleException(e).message)
        }
        viewStatusLiveData.postValue(viewStatusLiveData.value)
        Log.e(
            " mvvmbasevm  onLoadFail",
            "onLoadFail msg==========: " + viewStatusLiveData.value!!.name
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestory() {
        onCleared()
        model!!.unRegister(this)
    } //    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)

    //    private void onResume() {
    //        dataList.postValue(dataList.getValue());
    //        viewStatusLiveData.postValue(viewStatusLiveData.getValue());
    //    }
    companion object {
        const val FAILED_NOT_LOGIN = -1001 //请先登录
    }

    init {
        dataList.value = ObservableArrayList()
        viewStatusLiveData.value = ViewStatus.LOADING
        errorMessage.value = ""
    }
}