package com.lapic.thomas.syncplayer.ui.splashscreen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;

import javax.inject.Inject;

import com.lapic.thomas.syncplayer.R;
import com.lapic.thomas.syncplayer.injection.component.ActivityComponent;
import com.lapic.thomas.syncplayer.ui.applications.ApplicationsActivity;
import com.lapic.thomas.syncplayer.ui.base.BaseMvpActivity;
import com.lapic.thomas.syncplayer.ui.mode.ModeActivity;
import com.lapic.thomas.syncplayer.ui.primarymode.PrimaryModeActivity;
import com.lapic.thomas.syncplayer.ui.secondarymode.SecondaryModeActivity;

public class SplashScreenActivity extends BaseMvpActivity<SplashScreenView, SplashScreenPresenter> implements SplashScreenView {

    @Inject
    protected SplashScreenPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @NonNull
    @Override
    public SplashScreenPresenter createPresenter() {
        return mPresenter;
    }

    @Override
    public void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public String getStringRes(int resId) {
        return getString(resId);
    }

    @Override
    public void callModeActivity(){
        startActivity(new Intent(this, ModeActivity.class));
        finish();
    }

    @Override
    public void callPrimaryModeActivity() {
        startActivity(new Intent(this, PrimaryModeActivity.class));
        finish();
    }

    @Override
    public void callSecondaryModeActivity() {
        startActivity(new Intent(this, SecondaryModeActivity.class));
        finish();
    }

    @Override
    public void callApplicationsActivity() {
        startActivity(new Intent(this, ApplicationsActivity.class));
        finish();
    }

}
