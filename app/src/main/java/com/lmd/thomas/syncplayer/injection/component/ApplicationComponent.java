package com.lmd.thomas.syncplayer.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import com.lmd.thomas.syncplayer.MyApplication;
import com.lmd.thomas.syncplayer.injection.ApplicationContext;
import com.lmd.thomas.syncplayer.injection.module.ApplicationModule;
import dagger.Component;

/**
 * Created by Thomas on 02/08/2017.
 **/
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(MyApplication myApplication);

    @ApplicationContext
    Context context();

    Application application();

//    AccountManager accountManager();

//    DataManager dataManager();
//
//    Retrofit retrofit();
//
//    EventBus eventBus();
}
