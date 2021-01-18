package com.arch.base.core.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.arch.base.core.R;
import com.arch.base.core.utils.LoadingUtil;
import com.arch.base.core.utils.ToastUtil;
import com.arch.base.core.viewmodel.MvvmBaseViewModel;
import com.arch.base.core.viewmodel.ViewStatus;

import java.util.Map;

//import com.arch.base.core.loadsir.EmptyCallback;
//import com.arch.base.core.loadsir.ErrorCallback;
//import com.arch.base.core.loadsir.LoadingCallback;
//import com.kingja.loadsir.callback.Callback;
//import com.kingja.loadsir.core.LoadService;
//import com.kingja.loadsir.core.LoadSir;


public abstract class MvvmFragment<V extends ViewDataBinding, VM extends MvvmBaseViewModel, D> extends Fragment implements Observer {
    protected VM viewModel;
    protected V viewDataBinding;
    protected String mFragmentTag = "";
    //    private LoadService mLoadService;
// 请求单个权限
    protected ActivityResultLauncher requestPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) {
                ToastUtil.show("获取权限成功");
            } else {
                ToastUtil.show("获取权限失败");
            }
        }
    });
    // 请求一组权限
    protected ActivityResultLauncher requestMultiplePermissions = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onActivityResult(Map<String, Boolean> result) {
//         result.entrySet().forEach(new Consumer<Map.Entry<String, Boolean>>() {
//             @Override
//             public void accept(Map.Entry<String, Boolean> stringBooleanEntry) {
//             }
//         });
            for (Map.Entry<String, Boolean> entry : result.entrySet()) {
                String mapKey = entry.getKey();
                Boolean mapValue = entry.getValue();
                System.out.println(mapKey + ":" + mapValue);
            }
        }
    });

    public abstract int getBindingVariable();

    public abstract
    @LayoutRes
    int getLayoutId();

    public abstract VM getViewModel();

    public abstract void onListItemInserted(ObservableList<D> sender);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onCreate");
        initParameters();
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onCreateView");
        return viewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onViewCreated");
        viewModel = getViewModel();
        if (viewModel != null) {
            getLifecycle().addObserver(viewModel);
            viewModel.dataList.observe(getViewLifecycleOwner(), this);
            viewModel.viewStatusLiveData.observe(getViewLifecycleOwner(), this);
            if (getBindingVariable() > 0) {
                viewDataBinding.setVariable(getBindingVariable(), viewModel);
                viewDataBinding.executePendingBindings();
            }
        }
    }

    /***
     *   初始化参数
     */
    protected void initParameters() {
    }

    public void showLoading() {
        LoadingUtil.createLoadingDialog(this.getActivity());
    }

    public void closeLoading() {
        LoadingUtil.closeDialog();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onActivityCreated");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(getContext());
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onDetach");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onResume");
    }

    @Override
    public void onDestroy() {
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        closeLoading();
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onDestroyView");
        super.onDestroyView();
    }

//    public void setLoadSir(View view) {
//        // You can change the callback on sub thread directly.
//        mLoadService = LoadSir.getDefault().register(view, new Callback.OnReloadListener() {
//            @Override
//            public void onReload(View v) {
//                onRetryBtnClick();
//            }
//        });
//    }

    protected abstract String getFragmentTag();

    public abstract void goToLogin();

    @Override
    public void onChanged(Object o) {
        if (o instanceof ViewStatus) {
            switch ((ViewStatus) o) {
                case TOKEN_ERROR:
                    goToLogin();
                    break;
                case LOADING:
//                    mLoadService.showCallback(LoadingCallback.class);
                    break;
                case EMPTY:
//                    mLoadService.showCallback(EmptyCallback.class);
                    break;
                case SHOW_CONTENT:
//                    mLoadService.showSuccess();
                    break;
                case NO_MORE_DATA:
                    ToastUtil.show(getString(R.string.no_more_data));
                    break;
                case REFRESH_ERROR:
//                    if (((ObservableArrayList) viewModel.dataList.getValue()).size() == 0) {
////                        mLoadService.showCallback(ErrorCallback.class);
//                        ToastUtil.show(getString(R.string.no_more_data));
//                    } else {
//                        ToastUtil.show(viewModel.errorMessage.getValue().toString());
//                    }
                    ToastUtil.show(viewModel.errorMessage.getValue().toString());
                    break;
                case LOAD_MORE_FAILED:
                    ToastUtil.show(viewModel.errorMessage.getValue().toString());
                    break;
            }
        } else if (o instanceof ObservableArrayList) {
            //屏蔽初始化的时候发送的通知
            if (viewModel.viewStatusLiveData.getValue() != null && viewModel.viewStatusLiveData.getValue() == ViewStatus.SHOW_CONTENT) {
                onListItemInserted((ObservableArrayList<D>) o);
            }
        }
    }

//    protected void showSuccess() {
//        if (mLoadService != null) {
//            mLoadService.showSuccess();
//        }
//    }
//
//    protected void showLoading() {
//        if (mLoadService != null) {
//            mLoadService.showCallback(LoadingCallback.class);
//        }
//    }
}
