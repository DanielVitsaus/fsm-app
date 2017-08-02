package br.com.lapic.thomas.fsm_app.ui.base;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by thomas on 02/08/17.
 */

public abstract class BaseMvpFragment<V extends MvpView, P extends MvpBasePresenter<V>>
        extends MvpFragment<V, P> {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
