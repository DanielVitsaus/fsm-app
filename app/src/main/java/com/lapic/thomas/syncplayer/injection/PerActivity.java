package com.lapic.thomas.syncplayer.injection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Thomas on 02/08/2017.
 **/
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
}
