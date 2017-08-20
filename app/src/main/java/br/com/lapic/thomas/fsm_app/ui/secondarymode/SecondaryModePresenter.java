package br.com.lapic.thomas.fsm_app.ui.secondarymode;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import br.com.lapic.thomas.fsm_app.helper.PreferencesHelper;

/**
 * Created by thomas on 19/08/17.
 */

public class SecondaryModePresenter extends MvpBasePresenter<SecondaryModeView> {

    @Inject
    protected PreferencesHelper mPreferencesHelper;

    @Inject
    public SecondaryModePresenter(PreferencesHelper preferencesHelper) {
        mPreferencesHelper = preferencesHelper;
    }

    public void onLeaveSecondaryMode() {
        mPreferencesHelper.clear();
        if (isViewAttached())
            getView().callModeActivity();
    }

    public void onShowHistoric() {
        if (isViewAttached())
            getView().showHistoric();
    }
}
