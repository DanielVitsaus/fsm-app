package br.com.lapic.thomas.syncplayer.injection.component;

import android.content.Context;

import br.com.lapic.thomas.syncplayer.injection.ActivityContext;
import br.com.lapic.thomas.syncplayer.injection.PerActivity;
import br.com.lapic.thomas.syncplayer.injection.module.ActivityModule;
import br.com.lapic.thomas.syncplayer.ui.mode.ModeActivity;
import br.com.lapic.thomas.syncplayer.ui.primarymode.PrimaryModeActivity;
import br.com.lapic.thomas.syncplayer.ui.secondarymode.SecondaryModeActivity;
import br.com.lapic.thomas.syncplayer.ui.splashscreen.SplashScreenActivity;
import dagger.Component;

/**
 * Created by Thomas on 02/08/2017.
 **/
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(SplashScreenActivity splashScreenActivity);

    void inject(ModeActivity modeActivity);

    void inject(PrimaryModeActivity primaryModeActivity);

    void inject(SecondaryModeActivity secondaryModeActivity);

    @ActivityContext
    Context context();

}
