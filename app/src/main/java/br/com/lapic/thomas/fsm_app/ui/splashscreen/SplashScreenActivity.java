package br.com.lapic.thomas.fsm_app.ui.splashscreen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;

import javax.inject.Inject;

import br.com.lapic.thomas.fsm_app.R;
import br.com.lapic.thomas.fsm_app.injection.component.ActivityComponent;
import br.com.lapic.thomas.fsm_app.ui.base.BaseMvpActivity;
import br.com.lapic.thomas.fsm_app.ui.main.MainActivity;

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
    public void callModeActivity(){
        startActivity(new Intent(this, MainActivity.class));
    }

}
