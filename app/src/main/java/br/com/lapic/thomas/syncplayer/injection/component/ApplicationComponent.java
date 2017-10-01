package br.com.lapic.thomas.syncplayer.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import br.com.lapic.thomas.syncplayer.MyApplication;
import br.com.lapic.thomas.syncplayer.injection.ApplicationContext;
import br.com.lapic.thomas.syncplayer.injection.module.ApplicationModule;
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
