package br.com.lapic.thomas.syncplayer.ui.applications;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import br.com.lapic.thomas.syncplayer.data.model.Media;

/**
 * Created by thomasmarquesbrandaoreis on 28/09/2017.
 */

public class ApplicationsPresenter
        extends MvpBasePresenter<ApplicationsView> {

    private ArrayList<ArrayList<Media>> listApplications;

    @Inject
    public ApplicationsPresenter() {}

    @Override
    public void attachView(ApplicationsView view) {
        super.attachView(view);
        getApplications();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
    }

    public void getApplications() {
    }
}
