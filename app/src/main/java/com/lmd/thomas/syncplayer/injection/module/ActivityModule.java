package com.lmd.thomas.syncplayer.injection.module;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.lmd.thomas.syncplayer.injection.ActivityContext;
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

    @Provides
    Handler provideHandler(){
        return new Handler(Looper.getMainLooper());
    }

}
