package com.arch.base.core.preference;

import android.app.Application;
import android.content.Context;

import com.tencent.mmkv.MMKV;


public class PreferencesUtil extends BasePreferences {
    public static final String HISTORY = "history";
    private static PreferencesUtil sInstance;

    public static PreferencesUtil getInstance() {
        if (sInstance == null) {
            synchronized (PreferencesUtil.class) {
                if (sInstance == null) {
                    sInstance = new PreferencesUtil();
                }
            }
        }
        return sInstance;
    }

    public static void init(Application application) {
        sApplication = application;
        MMKV.initialize(application);
    }


    @Override
    public String getSPFileName() {
        return "common_data";
    }

    public static final String KEY_LOGIN_BEAN = "KEY_LOGIN_BEAN";

    private static final String CONFIG = "config";
    private static final String APP_CONFIG = "app_config";
    private static MMKV sp;

    private static MMKV appConfigSp;

    public String getUser() {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = MMKV.mmkvWithID(KEY_LOGIN_BEAN, Context.MODE_PRIVATE);
        }
        return sp.getString(KEY_LOGIN_BEAN, "");
    }

    public void putUser(String loginBean) {
        if (sp == null) {
            sp = MMKV.mmkvWithID(KEY_LOGIN_BEAN, Context.MODE_PRIVATE);
        }
        sp.edit().putString(KEY_LOGIN_BEAN, loginBean).commit();

//        String json = new Gson().toJson(loginBean);
    }

    /**
     * 写入boolean变量至sp中
     *
     * @param key   存储节点名称
     * @param value 存储节点的值string
     */
    public void putCodeString(String key, String value) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = MMKV.mmkvWithID(CONFIG, Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }

    public void putCodeInt(String key, int value) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = MMKV.mmkvWithID(CONFIG, Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key, value).commit();
    }

    public int getCodeInt(String key) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = MMKV.mmkvWithID(CONFIG, Context.MODE_PRIVATE);
        }
        if (sp == null) {
            sp = MMKV.mmkvWithID(CONFIG, Context.MODE_PRIVATE);
        }
        return sp.getInt("key", 0);
    }

    public String getToken() {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = MMKV.mmkvWithID(APP_CONFIG, Context.MODE_PRIVATE);
        }
        return sp.getString("token", "");
    }

    /**
     * 读取boolean标示从sp中
     *
     * @param key      存储节点名称
     * @param defValue 没有此节点默认值
     * @return 默认值或者此节点读取到的结果
     */
    public String getCodeString(String key, String defValue) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = MMKV.mmkvWithID(CONFIG, Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }

    public void putAppStatusString(String key, String value) {
        if (appConfigSp == null) {
            appConfigSp = MMKV.mmkvWithID(APP_CONFIG, Context.MODE_PRIVATE);
        }
        appConfigSp.edit().putString(key, value).commit();
    }

    public String getAppStatusString(String key, String defValue) {
        if (appConfigSp == null) {
            appConfigSp = MMKV.mmkvWithID(APP_CONFIG, Context.MODE_PRIVATE);
        }
        return appConfigSp.getString(key, defValue);
    }

    public void putAppStatusBoolean(String key, boolean value) {
        if (appConfigSp == null) {
            appConfigSp = MMKV.mmkvWithID(APP_CONFIG, Context.MODE_PRIVATE);
        }
        appConfigSp.edit().putBoolean(key, value).commit();
    }

    public boolean getAppStatusBoolean(String key, boolean defValue) {
        //(存储节点文件名称,读写方式)
        if (appConfigSp == null) {
            appConfigSp = MMKV.mmkvWithID(APP_CONFIG, Context.MODE_PRIVATE);
        }
        return appConfigSp.getBoolean(key, defValue);
    }
}
