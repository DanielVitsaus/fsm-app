package br.com.lapic.thomas.syncplayer.ui.base;

import android.content.Context;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import br.com.lapic.thomas.syncplayer.MyApplication;
import br.com.lapic.thomas.syncplayer.injection.component.ActivityComponent;
import br.com.lapic.thomas.syncplayer.injection.component.DaggerActivityComponent;
import br.com.lapic.thomas.syncplayer.injection.module.ActivityModule;

/**
 * Created by thomas on 02/08/17.
 */

public abstract class BaseMvpLceFragment<CV extends View, M, V extends MvpLceView<M>, P extends MvpBasePresenter<V>>
        extends MvpLceFragment<CV, M, V, P> {

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
