package com.emcrp.network;

import com.arch.base.core.model.ServerException;
import com.arch.base.core.preference.PreferencesUtil;
import com.blankj.utilcode.util.DeviceUtils;
import com.emcrp.network.base.NetworkApi;
import com.emcrp.network.beans.BaseResponse;
import com.emcrp.network.utils.TecentUtil;

import io.reactivex.rxjava3.functions.Function;
import okhttp3.Interceptor;
import okhttp3.Request;

public class WanAndroidApi extends NetworkApi {
    private static volatile WanAndroidApi sInstance;

    public static WanAndroidApi getInstance() {
        if (sInstance == null) {
            synchronized (WanAndroidApi.class) {
                if (sInstance == null) {
                    sInstance = new WanAndroidApi();
                }
            }
        }
        return sInstance;
    }

    public static <T> T getService(Class<T> service) {
        return getInstance().getRetrofit(service).create(service);
    }

    @Override
    protected Interceptor getInterceptor() {
        return chain -> {
            String timeStr = TecentUtil.getTimeStr();
            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("Source", "source");
            builder.addHeader("Date", timeStr);
            builder.addHeader("Content-Type", "application/json;charset=utf-8");
//            builder.addHeader("Authorization", "Bearer " + PreferencesUtil.getInstance().getToken());
            builder.addHeader("Accept-Language", "en" + ";q=1");
            builder.addHeader("Accept", "application/json");
            builder.addHeader("User-Agent", "android " + DeviceUtils.getSDKVersionCode() + "");
            builder.addHeader("deviceId", DeviceUtils.getModel() + " SDKVersionName=" + DeviceUtils.getSDKVersionName() + " DeviceId=" + DeviceUtils.getUniqueDeviceId());
            return chain.proceed(builder.build());
        };
    }

    @Override
    protected <T> Function<T, T> getAppErrorHandler() {
        return response -> {
            //response中code码不回0出现错误
            if (response instanceof BaseResponse && ((BaseResponse) response).errorCode != 0) {

                ServerException exception
                      = new ServerException();
                exception.code = ((BaseResponse) response).errorCode;
                exception.msg = ((BaseResponse) response).errorMsg != null ? ((BaseResponse) response).errorMsg : "";
                throw exception;

            }
            return response;
        };
    }

    @Override
    public String getFormal() {
        return "https://www.wanandroid.com/";
    }

    @Override
    public String getTest() {
        return "https://www.wanandroid.com/";
    }
}

