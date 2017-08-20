package br.com.lapic.thomas.fsm_app.ui.mode;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import javax.inject.Inject;

import br.com.lapic.thomas.fsm_app.R;
import br.com.lapic.thomas.fsm_app.helper.PreferencesHelper;

/**
 * Created by thomas on 02/08/17.
 */

public class ModePresenter
        extends MvpBasePresenter<ModeView>
        implements MvpPresenter<ModeView> {

    private PreferencesHelper mPreferencesHelper;

    @Inject
    public ModePresenter(PreferencesHelper preferencesHelper) {
        mPreferencesHelper = preferencesHelper;
    }

    @Override
    public void attachView(ModeView view) {
        super.attachView(view);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
    }

    public void onClickButton(String mode) {
        mPreferencesHelper.putMode(mode);
        if (isViewAttached()) {
            if (mPreferencesHelper.getMode().equals(getView().getStringRes(R.string.primary_mode)))
                getView().callPrimaryModeActivity();
            else
                getView().callSecondaryModeActivity();
        }
    }
}
