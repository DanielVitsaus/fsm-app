package com.lapic.thomas.syncplayer.injection.component;

import android.content.Context;

import com.lapic.thomas.syncplayer.injection.ActivityContext;
import com.lapic.thomas.syncplayer.injection.PerActivity;
import com.lapic.thomas.syncplayer.injection.module.ActivityModule;
import com.lapic.thomas.syncplayer.ui.applications.ApplicationsActivity;
import com.lapic.thomas.syncplayer.ui.mode.ModeActivity;
import com.lapic.thomas.syncplayer.ui.primarymode.PrimaryModeActivity;
import com.lapic.thomas.syncplayer.ui.secondarymode.SecondaryModeActivity;
import com.lapic.thomas.syncplayer.ui.settings.SettingsActivity;
import com.lapic.thomas.syncplayer.ui.splashscreen.SplashScreenActivity;
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

    void inject(ApplicationsActivity applicationsActivity);

    void inject(SettingsActivity settingsActivity);

    @ActivityContext
    Context context();

}
