package br.com.lapic.thomas.fsm_app.ui.base;

import android.os.Bundle;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by thomas on 02/08/17.
 */

public abstract class BaseMvpActivity<V extends MvpView, P extends MvpBasePresenter<V>>
        extends MvpActivity<V, P> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
