package com.evento.team2.eventspack.modules;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.interactors.PreferencesInteractorImpl;
import com.evento.team2.eventspack.interactors.interfaces.PreferencesInteractor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Daniel on 12-Jan-16.
 */
@Module
public class InteractorsModule {

    @Provides
    @Singleton
    PreferencesInteractor providePreferencesInteractor(EventiApplication eventiApplication) {
        return new PreferencesInteractorImpl(eventiApplication);
    }
}
