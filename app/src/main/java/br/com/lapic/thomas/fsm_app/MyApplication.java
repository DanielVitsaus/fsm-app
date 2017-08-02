package br.com.lapic.thomas.fsm_app;

import android.app.Application;
import android.content.Context;

import br.com.lapic.thomas.fsm_app.injection.component.ApplicationComponent;
import br.com.lapic.thomas.fsm_app.injection.component.DaggerApplicationComponent;
import br.com.lapic.thomas.fsm_app.injection.module.ApplicationModule;

/**
 * Created by Thomas on 02/08/2017.
 */

public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getSimpleName();
    ApplicationComponent mApplicationComponent;
    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        mApplicationComponent.inject(this);
    }

    public static Application getApplication() {
        return application;
    }

    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }


}
