package com.evento.team2.eventspack.interactors;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.interactors.interfaces.PreferencesInteractor;

/**
 * Created by Daniel on 12-Jan-16.
 */
public class PreferencesInteractorImpl implements PreferencesInteractor {

    private static String SHARED_PREFERENCE_NAME = "eventi_preferences";
    private static String SHARED_PREFERENCE_LAST_UPDATE_OF_EVENTS = "last_update_of_events";

    private SharedPreferences preferences;

    public PreferencesInteractorImpl(EventiApplication eventiApplication) {
        preferences = eventiApplication.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public String getLastUpdateOfEvents() {
        return preferences.getString(SHARED_PREFERENCE_LAST_UPDATE_OF_EVENTS, "");
    }

    @Override
    public void setLastUpdateOfEvents(String todayDate) {
        preferences.edit().putString(SHARED_PREFERENCE_LAST_UPDATE_OF_EVENTS, todayDate).apply();
    }
}
