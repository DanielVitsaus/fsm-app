package com.lmd.thomas.syncplayer.injection.component;

import android.content.Context;

import com.lmd.thomas.syncplayer.injection.ActivityContext;
import com.lmd.thomas.syncplayer.injection.PerActivity;
import com.lmd.thomas.syncplayer.injection.module.ActivityModule;
import com.lmd.thomas.syncplayer.ui.applications.ApplicationsActivity;
import com.lmd.thomas.syncplayer.ui.mode.ModeActivity;
import com.lmd.thomas.syncplayer.ui.primarymode.PrimaryModeActivity;
import com.lmd.thomas.syncplayer.ui.secondarymode.SecondaryModeActivity;
import com.lmd.thomas.syncplayer.ui.settings.SettingsActivity;
import com.lmd.thomas.syncplayer.ui.splashscreen.SplashScreenActivity;
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
