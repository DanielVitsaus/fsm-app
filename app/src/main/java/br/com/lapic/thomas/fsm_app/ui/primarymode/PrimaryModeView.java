package br.com.lapic.thomas.fsm_app.ui.primarymode;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.ArrayList;

import br.com.lapic.thomas.fsm_app.data.model.Media;

/**
 * Created by thomas on 19/08/17.
 */

public interface PrimaryModeView extends MvpView {

    void callModeActivity();

    void showLoading(int resIdMessage);

    void hideLoading();

    void showContent();

    void showError(int resId);

    void setListMedias(ArrayList<Media> medias);

    void checkPermissions();

    Context getMyContext();

    void callPlayer();
}
