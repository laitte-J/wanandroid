package com.arch.base.common.arouter.wall;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.template.IProvider;


public interface IWallService extends IProvider {
    String WALL_ROUTER = "/wall/";
    String WALL_SERVICE = WALL_ROUTER + "wall_service";
    Fragment getWallFragment();
    Fragment getHomeFragment();
    void show();
}
