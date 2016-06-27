package com.simplemobiletools.camera;

import android.content.Context;
import android.content.SharedPreferences;

public class Config {
    private SharedPreferences mPrefs;

    public static Config newInstance(Context context) {
        return new Config(context);
    }

    public Config(Context context) {
        mPrefs = context.getSharedPreferences(Constants.PREFS_KEY, Context.MODE_PRIVATE);
    }

    public boolean getLongTapEnabled() {
        return mPrefs.getBoolean(Constants.LONG_TAP, true);
    }

    public void setLongTapEnabled(boolean enabled) {
        mPrefs.edit().putBoolean(Constants.LONG_TAP, enabled).apply();
    }

    public boolean getFocusBeforeCaptureEnabled() {
        return mPrefs.getBoolean(Constants.FOCUS_BEFORE_CAPTURE, false);
    }

    public void setFocusBeforeCaptureEnabled(boolean enabled) {
        mPrefs.edit().putBoolean(Constants.FOCUS_BEFORE_CAPTURE, enabled).apply();
    }
}