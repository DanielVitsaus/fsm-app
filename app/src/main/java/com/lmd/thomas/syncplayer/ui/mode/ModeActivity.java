package com.lmd.thomas.syncplayer.ui.mode;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import javax.inject.Inject;

import com.lmd.thomas.syncplayer.R;
import com.lmd.thomas.syncplayer.injection.component.ActivityComponent;
import com.lmd.thomas.syncplayer.ui.applications.ApplicationsActivity;
import com.lmd.thomas.syncplayer.ui.base.BaseMvpActivity;
import com.lmd.thomas.syncplayer.ui.primarymode.PrimaryModeActivity;
import com.lmd.thomas.syncplayer.ui.secondarymode.SecondaryModeActivity;
import butterknife.ButterKnife;

public class ModeActivity
        extends BaseMvpActivity<ModeView, ModePresenter>
        implements ModeView {

    @Inject
    protected ModePresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        ButterKnife.bind(this);
    }

    @NonNull
    @Override
    public ModePresenter createPresenter() {
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
    public void onClickButton(View view) {
        String tagButton = view.getTag().toString();
        mPresenter.onClickButton(tagButton);
    }

    @Override
    public void callPrimaryModeActivity() {
        startActivity(new Intent(this, PrimaryModeActivity.class));
        finish();
    }

    @Override
    public void callApplicationsActivity() {
        startActivity(new Intent(this, ApplicationsActivity.class));
        finish();
    }

    @Override
    public void callSecondaryModeActivity() {
        startActivity(new Intent(this, SecondaryModeActivity.class));
        finish();
    }

}
