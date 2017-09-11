package br.com.lapic.thomas.fsm_app.ui.mode;

import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by thomas on 02/08/17.
 */

public interface ModeView
        extends MvpView {

    String getStringRes(int resId);

    void onClickButton(View view);

    void callPrimaryModeActivity();

    void callSecondaryModeActivity();
}
