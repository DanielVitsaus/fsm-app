package com.lmd.thomas.syncplayer.ui.settings;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import com.lmd.thomas.syncplayer.helper.PreferencesHelper;

/**
 * Created by thomasmarquesbrandaoreis on 17/12/2017.
 */

public class SettingsPresenter extends MvpBasePresenter<SettingsView> {

    @Inject
    protected PreferencesHelper mPreferencesHelper;

    @Inject
    public SettingsPresenter(PreferencesHelper preferencesHelper) {
        mPreferencesHelper = preferencesHelper;
    }

    public void setSupportPng(boolean value) {
        mPreferencesHelper.putSupportPng(value);
    }

    public boolean getSupportPng() {
        return mPreferencesHelper.getSupportPng();
    }

    public void setSupportJpeg(boolean value) {
        mPreferencesHelper.putSupportJpeg(value);
    }

    public boolean getSupportJpeg() {
        return mPreferencesHelper.getSupportJpeg();
    }

    public void setSupportMpeg4(boolean value) {
        mPreferencesHelper.putSupportMpeg4(value);
    }

    public boolean getSupportMpeg4() {
        return mPreferencesHelper.getSupportMpeg4();
    }

    public void setSupport3gp(boolean value) {
        mPreferencesHelper.putSupport3gp(value);
    }

    public boolean getSupport3gp() {
        return mPreferencesHelper.getSupport3gp();
    }

    public void setSupportMov(boolean value) {
        mPreferencesHelper.putSupportMov(value);
    }

    public boolean getSupportMov() {
        return mPreferencesHelper.getSupportMov();
    }

    public void setSupportMpeg3(boolean value) {
        mPreferencesHelper.putSupportMpeg3(value);
    }

    public boolean getSupportMpeg3() {
        return mPreferencesHelper.getSupportMpeg3();
    }

    public void setSupportWav(boolean value) {
        mPreferencesHelper.putSupportWav(value);
    }

    public boolean getSupportWav() {
        return mPreferencesHelper.getSupportWav();
    }

    public void setSupportApp(boolean value) {
        mPreferencesHelper.putSupportApp(value);
    }

    public boolean getSupportApp() {
        return mPreferencesHelper.getSupportApp();
    }

}
