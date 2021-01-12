package com.emcrp.network.base;

import com.emcrp.network.commoninterceptor.CommonRequestInterceptor;
import com.emcrp.network.commoninterceptor.CommonResponseInterceptor;
import com.emcrp.network.environment.EnvironmentActivity;
import com.emcrp.network.environment.IEnvironment;
import com.emcrp.network.errorhandler.HttpErrorHandler;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.HashMap;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class NetworkApi implements IEnvironment {
    private static INetworkRequiredInfo iNetworkRequiredInfo;
    private static HashMap<String, Retrofit> retrofitHashMap = new HashMap<>();
    private String mBaseUrl;
    private OkHttpClient mOkHttpClient;
    private static boolean mIsFormal = true;
    private ClearableCookieJar cookieJar;

    public NetworkApi() {
        if (!mIsFormal) {
            mBaseUrl = getTest();
        }
        mBaseUrl = getFormal();
    }

    public static void init(INetworkRequiredInfo networkRequiredInfo) {
        iNetworkRequiredInfo = networkRequiredInfo;
        mIsFormal = EnvironmentActivity.isOfficialEnvironment(networkRequiredInfo.getApplicationContext());

    }

    protected Retrofit getRetrofit(Class service) {
        if (retrofitHashMap.get(mBaseUrl + service.getName()) != null) {
            return retrofitHashMap.get(mBaseUrl + service.getName());
        }
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.baseUrl(mBaseUrl);
        retrofitBuilder.client(getOkHttpClient());
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
        //call转obscribe
        retrofitBuilder.addCallAdapterFactory(RxJava3CallAdapterFactory.create());
        Retrofit retrofit = retrofitBuilder.build();
        retrofitHashMap.put(mBaseUrl + service.getName(), retrofit);
        return retrofit;
    }

    private OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            if (getInterceptor() != null) {
                okHttpClientBuilder.addInterceptor(getInterceptor());
            }
            int cacheSize = 100 * 1024 * 1024; // 10MB
            okHttpClientBuilder.cache(new Cache(iNetworkRequiredInfo.getApplicationContext().getCacheDir(), cacheSize));
            okHttpClientBuilder.addInterceptor(new CommonRequestInterceptor(iNetworkRequiredInfo));
            okHttpClientBuilder.addInterceptor(new CommonResponseInterceptor());

            if (iNetworkRequiredInfo != null && (iNetworkRequiredInfo.isDebug())) {
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
                cookieJar =
                        new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(iNetworkRequiredInfo.getApplicationContext()));
            }
            if (cookieJar != null)
                okHttpClientBuilder.cookieJar(cookieJar);
            mOkHttpClient = okHttpClientBuilder.build();
        }
        return mOkHttpClient;

    }

    public <T> ObservableTransformer<T, T> applySchedulers(final Observer<T> observer) {
        return (ObservableTransformer<T, T>) upstream -> {
            Observable<T> observable = (Observable<T>) upstream
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(getAppErrorHandler())
                    .onErrorResumeNext(new HttpErrorHandler<T>());
            observable.subscribe(observer);
            return observable;
        };
    }

    protected abstract Interceptor getInterceptor();

    protected abstract <T> Function<T, T> getAppErrorHandler();
}
