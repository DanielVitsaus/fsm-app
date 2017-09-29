package br.com.lapic.thomas.syncplayer.ui.applications;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import br.com.lapic.thomas.syncplayer.data.model.App;

/**
 * Created by thomasmarquesbrandaoreis on 28/09/2017.
 */

public interface ApplicationsView extends MvpView {
    String getStringRes(int resId);

    void setListApplications(List<App> listApplications);

    void showError(int resId);
}
