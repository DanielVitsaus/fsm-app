package com.lmd.thomas.syncplayer.ui.base;

import android.os.Bundle;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import com.lmd.thomas.syncplayer.MyApplication;
import com.lmd.thomas.syncplayer.injection.component.ActivityComponent;
import com.lmd.thomas.syncplayer.injection.component.DaggerActivityComponent;
import com.lmd.thomas.syncplayer.injection.module.ActivityModule;

/**
 * Created by thomas on 02/08/17.
 */

public abstract class BaseMvpActivity<V extends MvpView, P extends MvpBasePresenter<V>>
        extends MvpActivity<V, P> {

    private ActivityComponent mActivityComponent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        injectComponent();
        super.onCreate(savedInstanceState);
    }

    private void injectComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(MyApplication.get(this).getComponent())
                    .build();
        }
        inject(mActivityComponent);
    }

    public abstract void inject(ActivityComponent activityComponent);

    public void showToast(int resIdMessage) {
        Toast.makeText(this, resIdMessage, Toast.LENGTH_LONG).show();
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
