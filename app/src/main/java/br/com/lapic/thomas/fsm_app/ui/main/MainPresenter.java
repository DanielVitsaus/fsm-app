package br.com.lapic.thomas.fsm_app.ui.main;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import javax.inject.Inject;

/**
 * Created by thomas on 02/08/17.
 */

public class MainPresenter
        extends MvpBasePresenter<MainView>
        implements MvpPresenter<MainView> {

    @Inject
    public MainPresenter(){}

    @Override
    public void attachView(MainView view) {
        super.attachView(view);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
    }
}
