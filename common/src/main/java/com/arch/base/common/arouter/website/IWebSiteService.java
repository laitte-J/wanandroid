package com.arch.base.common.arouter.website;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.template.IProvider;

public interface IWebSiteService extends IProvider {
    String NEWS_ROUTER = "/website/";
    String NEWS_SERVICE = NEWS_ROUTER + "website_service";
    Fragment getWebSiteFragment();
}
