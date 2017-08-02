package br.com.lapic.thomas.fsm_app.ui.base;

import android.os.Bundle;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceActivity;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

/**
 * Created by thomas on 02/08/17.
 */

public abstract class BaseMvpLceActivity<CV extends View, M, V extends MvpLceView<M>, P extends MvpBasePresenter<V>>
        extends MvpLceActivity<CV, M, V, P> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
