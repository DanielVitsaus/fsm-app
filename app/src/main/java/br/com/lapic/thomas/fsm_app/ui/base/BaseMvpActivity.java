package br.com.lapic.thomas.fsm_app.ui.base;

import android.os.Bundle;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import br.com.lapic.thomas.fsm_app.MyApplication;
import br.com.lapic.thomas.fsm_app.injection.component.ActivityComponent;
import br.com.lapic.thomas.fsm_app.injection.component.DaggerActivityComponent;
import br.com.lapic.thomas.fsm_app.injection.module.ActivityModule;

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

}
