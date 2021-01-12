package com.arch.base.common.arouter;

import android.content.Context;

import com.alibaba.android.arouter.facade.template.IProvider;

public interface IUserInstallService extends IProvider {

    String WEB_ROUTER = "/web/";
    String WEB_SERVICE = WEB_ROUTER + "web_service";
    void launch(Context context, String extra);
}
