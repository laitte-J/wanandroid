package com.arch.base.core.preference;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.arch.base.core.utils.EditorUtils;
import com.arch.base.core.utils.Utils;
import com.tencent.mmkv.MMKV;

import java.util.Map;


abstract class BasePreferences {
    protected static Application sApplication;

    protected MMKV mPreference;

    public BasePreferences() {
        if (Utils.isEmpty(getSPFileName())) {
            mPreference = MMKV.mmkvWithID("netData");
        } else {
            mPreference =MMKV.mmkvWithID(getSPFileName(), Context.MODE_PRIVATE);
        }
    }

    protected abstract String getSPFileName();

    public String getString(String key, String defValue) {
        return mPreference.getString(key, defValue);
    }

    public Boolean getBoolean(String key, boolean defBool) {
        return mPreference.getBoolean(key, defBool);
    }

    public void setBoolean(String key, boolean bool) {
        mPreference.putBoolean(key, bool);
    }

    public void setLong(String key, long value) {
        mPreference.putLong(key, value);
    }

    public long getLong(String key, long defValue) {
        return mPreference.getLong(key, defValue);
    }

    public String getString(String key) {
        return mPreference.getString(key, "");
    }

    public void setString(String key, String value) {
        mPreference.putString(key, value);
    }

    public int getInt(String key, int defaultVal) {
        return mPreference.getInt(key, defaultVal);
    }

    public void setInt(String key, int value) {
        mPreference.putInt(key, value);
    }

    public void remove(String key) {
        if (!TextUtils.isEmpty(key) && mPreference.contains(key)) {
            mPreference.remove(key);
        }
    }

    public boolean contains(String key) {
        return mPreference.contains(key);
    }

    public void clearAll() {
        mPreference.clear();
    }

    public Map<String, ?> getAll() {
        return mPreference.getAll();
    }
}
