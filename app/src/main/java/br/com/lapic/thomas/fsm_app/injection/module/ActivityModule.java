package br.com.lapic.thomas.fsm_app.injection.module;

import android.app.Activity;
import android.content.Context;

import br.com.lapic.thomas.fsm_app.injection.ActivityContext;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Thomas on 02/08/2017.
 **/
@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

}
