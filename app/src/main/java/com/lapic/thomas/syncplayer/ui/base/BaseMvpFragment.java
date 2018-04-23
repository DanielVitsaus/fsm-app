package com.lapic.thomas.syncplayer.ui.base;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpView;

import com.lapic.thomas.syncplayer.MyApplication;
import com.lapic.thomas.syncplayer.injection.component.ActivityComponent;
import com.lapic.thomas.syncplayer.injection.component.DaggerActivityComponent;
import com.lapic.thomas.syncplayer.injection.module.ActivityModule;

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
