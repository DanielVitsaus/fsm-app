package com.lapic.thomas.syncplayer.ui.splashscreen;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by thomas on 19/08/17.
 */

public interface SplashScreenView extends MvpView {

    String getStringRes(int resId);
    
    void callModeActivity();

    void callPrimaryModeActivity();

    void callSecondaryModeActivity();

    void callApplicationsActivity();
}
