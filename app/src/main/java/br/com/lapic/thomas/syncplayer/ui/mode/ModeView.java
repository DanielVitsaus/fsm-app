package br.com.lapic.thomas.syncplayer.ui.mode;

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

    void callApplicationsActivity();

    void callSecondaryModeActivity();
}
