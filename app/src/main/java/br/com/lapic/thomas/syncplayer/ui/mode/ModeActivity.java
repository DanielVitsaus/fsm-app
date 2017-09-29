package br.com.lapic.thomas.syncplayer.ui.mode;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import javax.inject.Inject;

import br.com.lapic.thomas.syncplayer.R;
import br.com.lapic.thomas.syncplayer.injection.component.ActivityComponent;
import br.com.lapic.thomas.syncplayer.ui.applications.ApplicationsActivity;
import br.com.lapic.thomas.syncplayer.ui.base.BaseMvpActivity;
import br.com.lapic.thomas.syncplayer.ui.primarymode.PrimaryModeActivity;
import br.com.lapic.thomas.syncplayer.ui.secondarymode.SecondaryModeActivity;
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
