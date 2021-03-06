package com.evento.team2.eventspack.modules;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.interactors.AlarmManagerInteractorImpl;
import com.evento.team2.eventspack.interactors.DatabaseInteractorImpl;
import com.evento.team2.eventspack.interactors.NotificationsInteractor;
import com.evento.team2.eventspack.interactors.PreferencesInteractorImpl;
import com.evento.team2.eventspack.interactors.interfaces.AlarmManagerInteractor;
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.interactors.interfaces.PreferencesInteractor;
import com.evento.team2.eventspack.services.TranslateService;
import com.evento.team2.eventspack.soapservice.ServiceEventoImpl;
import com.evento.team2.eventspack.soapservice.interfaces.ServiceEvento;
import com.evento.team2.eventspack.utils.NetworkUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        return new NotificationsInteractor(alarmManagerInteractor);
    }

    @Provides
    @Singleton
    ServiceEvento provideServiceEvento(EventiApplication eventiApplication, DatabaseInteractor databaseInteractor) {
        return new ServiceEventoImpl(eventiApplication, databaseInteractor);
    }

    @Provides
    @Singleton
    TranslateService provideTranslateService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(TranslateService.class);
    }
}
