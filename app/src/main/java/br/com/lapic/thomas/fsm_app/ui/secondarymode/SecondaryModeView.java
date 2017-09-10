package br.com.lapic.thomas.fsm_app.ui.secondarymode;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by thomas on 19/08/17.
 */

public interface SecondaryModeView extends MvpView {

    void checkPermissions();

    void callModeActivity();

    void showLoading(int resIdMessage);

    void hideLoading();

    void showError(int resId);

    void showMessage(String message);

    void showHistoric();

    void showDialogChoiceGroup(int amountGroups);

    Context getMyContext();

    void startFragmentPlayer(Bundle bundle);

    void startDownloadMedias();

    void incrementProgressBar(int value);
}
