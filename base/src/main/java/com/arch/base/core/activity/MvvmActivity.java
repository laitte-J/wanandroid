package com.arch.base.core.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

import com.arch.base.core.R;
import com.arch.base.core.utils.LoadingUtil;
import com.arch.base.core.utils.ToastUtil;
import com.arch.base.core.viewmodel.MvvmBaseViewModel;
import com.arch.base.core.viewmodel.ViewStatus;

import java.util.Map;


public abstract class MvvmActivity<V extends ViewDataBinding, VM extends MvvmBaseViewModel, D> extends FragmentActivity implements Observer {
    protected VM viewModel;
    protected V viewDataBinding;
    protected String TAG = getActivityTag();
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
    protected   ActivityResultLauncher requestMultiplePermissions = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onActivityResult(Map<String, Boolean> result) {
//         result.entrySet().forEach(new Consumer<Map.Entry<String, Boolean>>() {
//             @Override
//             public void accept(Map.Entry<String, Boolean> stringBooleanEntry) {
//             }
//         });
            for(Map.Entry<String, Boolean> entry : result.entrySet()){
                String mapKey = entry.getKey();
                Boolean mapValue = entry.getValue();
                System.out.println(mapKey+":"+mapValue);
            }
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
        performDataBinding();
        if (viewModel != null) {
            getLifecycle().addObserver(viewModel);
            viewModel.errorMessage.observe(this, this);
            viewModel.viewStatusLiveData.observe(this, this);
            viewModel.dataList.observe(this, this);
        }
    }

    public void showLoading() {
        LoadingUtil.createLoadingDialog(this);
    }

    public void closeLoading() {
        LoadingUtil.closeDialog();
    }
//    bindToLifecycle

    private void initViewModel() {
        viewModel = getViewModel();
    }


    protected abstract VM getViewModel();

    public abstract int getBindingVariable();

    public abstract void goToLogin();

    public abstract void onListItemInserted(ObservableList<D> sender);

    public abstract
    @LayoutRes
    int getLayoutId();

    private void performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        this.viewModel = viewModel == null ? getViewModel() : viewModel;
        if (getBindingVariable() > 0) {
            viewDataBinding.setVariable(getBindingVariable(), viewModel);
        }
        viewDataBinding.executePendingBindings();
    }

    @Override
    public void onChanged(Object o) {
        if (o instanceof ViewStatus) {
            switch ((ViewStatus) o) {
                case TOKEN_ERROR:
                    goToLogin();
//                    mLoadService.showCallback(LoadingCallback.class);
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
                    if (((ObservableArrayList) viewModel.dataList.getValue()).size() == 0) {
                        ToastUtil.show(viewModel.errorMessage.getValue().toString());
                    } else {
                        ToastUtil.show(viewModel.errorMessage.getValue().toString());
                    }
                    break;
                case LOAD_MORE_FAILED:
                    ToastUtil.show(viewModel.errorMessage.getValue().toString());
                    break;
            }
        } else if (o instanceof ObservableArrayList) {
            //屏蔽初始化的时候发送的通知
            if (viewModel.viewStatusLiveData.getValue() != ViewStatus.LOADING) {
                onListItemInserted((ObservableArrayList<D>) o);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(getActivityTag(), "Activity:" + this + ": " + "onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(getActivityTag(), "Activity:" + this + ": " + "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(getActivityTag(), "Activity:" + this + ": " + "onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeLoading();
        Log.d(getActivityTag(), "Activity:" + this + ": " + "onDestroy");
    }

    /*PromptView end*/
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected String getActivityTag() {
        return this.getClass().getSimpleName();
    }

}
