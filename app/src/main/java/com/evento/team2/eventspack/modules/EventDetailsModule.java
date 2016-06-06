package com.evento.team2.eventspack.modules;

import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.interactors.interfaces.NotificationsInteractor;
import com.evento.team2.eventspack.presenters.FragmentEventDetailsPresenterImpl;
import com.evento.team2.eventspack.presenters.interfaces.FragmentEventDetailsPresenter;
import com.evento.team2.eventspack.scopes.EventDetailsScope;
import com.evento.team2.eventspack.utils.interfaces.MainThread;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Daniel on 06-Mar-16.
 */
@Module
public class EventDetailsModule {

    @Provides
    @EventDetailsScope
    FragmentEventDetailsPresenter provideFragmentEventDetailsPresenter(MainThread mainThread,
                                                                       DatabaseInteractor databaseInteractor,
                                                                       NotificationsInteractor notificationsInteractor) {
        return new FragmentEventDetailsPresenterImpl(mainThread, databaseInteractor, notificationsInteractor);
    }
}
