package com.evento.team2.eventspack.modules;

import com.evento.team2.eventspack.interactors.NotificationsInteractor;
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.presenters.EventDetailsPresenter;
import com.evento.team2.eventspack.scopes.EventDetailsScope;
import com.evento.team2.eventspack.services.TranslateService;
import com.evento.team2.eventspack.utils.interfaces.MainThread;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Daniel on 06-Mar-16.
 */
@Module
public class EventDetailsModule {

    @Provides
    @EventDetailsScope
    EventDetailsPresenter provideFragmentEventDetailsPresenter(MainThread mainThread, DatabaseInteractor databaseInteractor,
                                                               NotificationsInteractor notificationsInteractor,
                                                               TranslateService translateService) {
        return new EventDetailsPresenter(mainThread, databaseInteractor, notificationsInteractor, translateService);
    }
}
