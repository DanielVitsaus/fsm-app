package br.com.lapic.thomas.fsm_app.ui.splashscreen;

import android.os.Handler;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

/**
 * Created by thomas on 19/08/17.
 */

public class SplashScreenPresenter extends MvpBasePresenter<SplashScreenView> {

    private final Handler mHandler;

    @Inject
    public SplashScreenPresenter(Handler handler){
        mHandler = handler;
    }

    @Override
    public void attachView(SplashScreenView view) {
        super.attachView(view);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getView().callModeActivity();
            }
        }, 10000);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
    }

}
