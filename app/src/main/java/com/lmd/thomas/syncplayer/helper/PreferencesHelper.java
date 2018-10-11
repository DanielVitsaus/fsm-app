package com.lmd.thomas.syncplayer.helper;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import com.lmd.thomas.syncplayer.injection.ApplicationContext;

/**
 * Created by thomas on 19/08/17.
 */

public class PreferencesHelper {

    private static final String PREF_FILE_NAME = "app_pref_file";
    private static final String KEY_MODE = "MODE";
    private static final String SUPPORT_PNG = "SUPPORT_PNG";
    private static final String SUPPORT_JPEG = "SUPPORT_JPEG";
    private static final String SUPPORT_MPEG4 = "SUPPORT_MPEG4";
    private static final String SUPPORT_3GP = "SUPPORT_3GP";
    private static final String SUPPORT_MOV = "SUPPORT_MOV";
    private static final String SUPPORT_MPEG3 = "SUPPORT_MPEG3";
    private static final String SUPPORT_WAV = "SUPPORT_WAV";
    private static final String SUPPORT_APP = "SUPPORT_APP";
    private static final String MEDIA_INDEX = "MEDIA_INDEX";

    private final SharedPreferences mPref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        mPref.edit().clear().apply();
    }

    public void putMode(String mode) {
        mPref.edit().putString(KEY_MODE, mode).apply();
    }

    public String getMode() {
        return mPref.getString(KEY_MODE, null);
    }

    public void clearMode() {
        mPref.edit().remove(KEY_MODE).apply();
    }


    public void putSupportPng(boolean value) {
        mPref.edit().putBoolean(SUPPORT_PNG, value).apply();
    }

    public boolean getSupportPng() {
        return mPref.getBoolean(SUPPORT_PNG,false);
    }

    public void putSupportJpeg(boolean value) {
        mPref.edit().putBoolean(SUPPORT_JPEG, value).apply();
    }

    public boolean getSupportJpeg() {
        return mPref.getBoolean(SUPPORT_JPEG, false);
    }

    public void putSupportMpeg4(boolean value) {
        mPref.edit().putBoolean(SUPPORT_MPEG4, value).apply();
    }

    public boolean getSupportMpeg4() {
        return mPref.getBoolean(SUPPORT_MPEG4, false);
    }

    public void putSupport3gp(boolean value) {
        mPref.edit().putBoolean(SUPPORT_3GP, value).apply();
    }

    public boolean getSupport3gp() {
        return mPref.getBoolean(SUPPORT_3GP, false);
    }

    public void putSupportMov(boolean value) {
        mPref.edit().putBoolean(SUPPORT_MOV, value).apply();
    }

    public boolean getSupportMov() {
        return mPref.getBoolean(SUPPORT_MOV, false);
    }

    public void putSupportMpeg3(boolean value) {
        mPref.edit().putBoolean(SUPPORT_MPEG3, value).apply();
    }

    public boolean getSupportMpeg3() {
        return mPref.getBoolean(SUPPORT_MPEG3, false);
    }

    public void putSupportWav(boolean value) {
        mPref.edit().putBoolean(SUPPORT_WAV, value).apply();
    }

    public boolean getSupportWav() {
        return mPref.getBoolean(SUPPORT_WAV, false);
    }

    public void putSupportApp(boolean value) {
        mPref.edit().putBoolean(SUPPORT_APP, value).apply();
    }

    public boolean getSupportApp() {
        return  mPref.getBoolean(SUPPORT_APP, false);
    }

    public void putMediaIndex(int value) {
        mPref.edit().putInt(MEDIA_INDEX, value).apply();
    }

    public int getMediaIndex() {
        return  mPref.getInt(MEDIA_INDEX, 0);
    }

}
