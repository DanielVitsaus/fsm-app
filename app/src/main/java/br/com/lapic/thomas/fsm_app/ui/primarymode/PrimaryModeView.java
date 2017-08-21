package br.com.lapic.thomas.fsm_app.ui.primarymode;

import android.content.res.AssetManager;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by thomas on 19/08/17.
 */

public interface PrimaryModeView extends MvpView {

    void callModeActivity();

    void showLoading();

    void hideLoading();

    void showError(int resId);

    AssetManager getAssetManager();

}
