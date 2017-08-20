package br.com.lapic.thomas.fsm_app.ui.secondarymode;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by thomas on 19/08/17.
 */

public interface SecondaryModeView extends MvpView {

    void callModeActivity();

    void showHistoric();

}
