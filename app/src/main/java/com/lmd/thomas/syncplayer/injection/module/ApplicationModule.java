package com.lmd.thomas.syncplayer.injection.module;

import android.app.Application;
import android.content.Context;

import com.lmd.thomas.syncplayer.injection.ApplicationContext;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Thomas on 02/08/2017.
 **/
@Module
public class ApplicationModule {

    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

}
