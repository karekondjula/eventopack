package com.evento.team2.eventspack.modules;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.interactors.AlarmManagerInteractorImpl;
import com.evento.team2.eventspack.interactors.DatabaseInteractorImpl;
import com.evento.team2.eventspack.interactors.NotificationsInteractorImpl;
import com.evento.team2.eventspack.interactors.PreferencesInteractorImpl;
import com.evento.team2.eventspack.interactors.interfaces.AlarmManagerInteractor;
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.interactors.interfaces.NotificationsInteractor;
import com.evento.team2.eventspack.interactors.interfaces.PreferencesInteractor;
import com.evento.team2.eventspack.soapservice.ServiceEventoImpl;
import com.evento.team2.eventspack.soapservice.interfaces.ServiceEvento;
import com.evento.team2.eventspack.utils.NetworkUtils;

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

    @Provides
    @Singleton
    DatabaseInteractor provideDatabaseInteractor(EventiApplication eventiApplication) {
        return new DatabaseInteractorImpl(eventiApplication);
    }

    @Provides
    @Singleton
    NetworkUtils provideNetworkUtils(EventiApplication eventiApplication) {
        return new NetworkUtils(eventiApplication);
    }

    @Provides
    @Singleton
    AlarmManagerInteractor provideAlarmManagerInteractor(EventiApplication eventiApplication) {
        return new AlarmManagerInteractorImpl(eventiApplication);
    }

    @Provides
    @Singleton
    NotificationsInteractor provideNotificationsInteractor(AlarmManagerInteractor alarmManagerInteractor) {
        return new NotificationsInteractorImpl(alarmManagerInteractor);
    }

    @Provides
    @Singleton
    ServiceEvento provideServiceEvento(DatabaseInteractor databaseInteractor) {
        return new ServiceEventoImpl(databaseInteractor);
    }
}
