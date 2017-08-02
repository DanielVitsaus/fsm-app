package br.com.lapic.thomas.fsm_app.ui.base;

import android.content.Context;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

/**
 * Created by thomas on 02/08/17.
 */

public abstract class BaseMvpLceFragment<CV extends View, M, V extends MvpLceView<M>, P extends MvpBasePresenter<V>>
        extends MvpLceFragment<CV, M, V, P> {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
