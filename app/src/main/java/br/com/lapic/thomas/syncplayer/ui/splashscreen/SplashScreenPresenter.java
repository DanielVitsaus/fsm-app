package br.com.lapic.thomas.syncplayer.ui.splashscreen;

import android.os.Handler;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import br.com.lapic.thomas.syncplayer.R;
import br.com.lapic.thomas.syncplayer.helper.PreferencesHelper;

/**
 * Created by thomas on 19/08/17.
 */

public class SplashScreenPresenter extends MvpBasePresenter<SplashScreenView> {

    private PreferencesHelper mPreferencesHelper;
    private final Handler mHandler;

    @Inject
    public SplashScreenPresenter(PreferencesHelper preferencesHelper, Handler handler){
        mPreferencesHelper = preferencesHelper;
        mHandler = handler;
    }

    @Override
    public void attachView(SplashScreenView view) {
        super.attachView(view);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String mode = mPreferencesHelper.getMode();
                if (mode == null)
                    getView().callModeActivity();
                else if (mode.equals(getView().getStringRes(R.string.primary_mode)))
//                    getView().callPrimaryModeActivity();
                    getView().callApplicationsActivity();
                else if (mode.equals(getView().getStringRes(R.string.secondary_mode)))
                    getView().callSecondaryModeActivity();
            }
        }, 2000);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
    }

}
