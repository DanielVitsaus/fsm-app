package br.com.lapic.thomas.syncplayer.ui.secondarymode;

import android.content.Context;
import android.os.Bundle;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.ArrayList;

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

    void showDialogChoiceGroup(int amountGroups, ArrayList<String> classesDevice);

    Context getMyContext();

    void startFragmentPlayer(Bundle bundle);

    void startDownloadMedias();

    void incrementProgressBar(int value);

}
