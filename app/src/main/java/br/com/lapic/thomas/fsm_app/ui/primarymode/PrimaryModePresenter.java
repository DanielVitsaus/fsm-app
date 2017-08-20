package br.com.lapic.thomas.fsm_app.ui.primarymode;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import br.com.lapic.thomas.fsm_app.helper.PreferencesHelper;

/**
 * Created by thomas on 19/08/17.
 */

public class PrimaryModePresenter
        extends MvpBasePresenter<PrimaryModeView> {

    @Inject
    protected PreferencesHelper mPreferencesHelper;

    @Inject
    public PrimaryModePresenter(PreferencesHelper preferencesHelper) {
        mPreferencesHelper = preferencesHelper;
    }

    public void onLeavePrimaryMode() {
        mPreferencesHelper.clear();
        if (isViewAttached())
            getView().callModeActivity();
    }
}
