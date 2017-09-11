package br.com.lapic.thomas.fsm_app.ui.base;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpView;

import br.com.lapic.thomas.fsm_app.MyApplication;
import br.com.lapic.thomas.fsm_app.injection.component.ActivityComponent;
import br.com.lapic.thomas.fsm_app.injection.component.DaggerActivityComponent;
import br.com.lapic.thomas.fsm_app.injection.module.ActivityModule;

/**
 * Created by thomas on 02/08/17.
 */

public abstract class BaseMvpFragment<V extends MvpView, P extends MvpBasePresenter<V>>
        extends MvpFragment<V, P> {

    private ActivityComponent mActivityComponent;

    @Override
    public void onAttach(Context context) {
        injectComponent();
        super.onAttach(context);
    }

    private void injectComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(getActivity()))
                    .applicationComponent(MyApplication.get(getActivity()).getComponent())
                    .build();
        }
        inject(mActivityComponent);
    }

    public abstract void inject(ActivityComponent activityComponent);

}
