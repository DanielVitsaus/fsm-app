package br.com.lapic.thomas.fsm_app.helper;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import br.com.lapic.thomas.fsm_app.injection.ApplicationContext;

/**
 * Created by thomas on 19/08/17.
 */

public class PreferencesHelper {

    private static final String PREF_FILE_NAME = "app_pref_file";
    private static final String KEY_MODE = "MODE";

    private final SharedPreferences mPref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        mPref.edit().clear().apply();
    }

    public void putMode(String mode) {
        mPref.edit().putString(KEY_MODE, mode).apply();
    }

    public String getMode() {
        return mPref.getString(KEY_MODE, null);
    }

    public void clearMode() {
        mPref.edit().remove(KEY_MODE).apply();
    }

}
