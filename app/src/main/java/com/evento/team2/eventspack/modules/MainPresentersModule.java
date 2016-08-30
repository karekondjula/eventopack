package com.evento.team2.eventspack.modules;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.interactors.NotificationsInteractor;
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.interactors.interfaces.PreferencesInteractor;
import com.evento.team2.eventspack.presenters.FragmentCategoriesPresenterImpl;
import com.evento.team2.eventspack.presenters.FragmentEventsPresenterImpl;
import com.evento.team2.eventspack.presenters.FragmentPlacesPresenterImpl;
import com.evento.team2.eventspack.presenters.FragmentSavedEventsPresenterImpl;
import com.evento.team2.eventspack.presenters.interfaces.FragmentCategoriesPresenter;
import com.evento.team2.eventspack.presenters.interfaces.FragmentEventsPresenter;
import com.evento.team2.eventspack.presenters.interfaces.FragmentPlacesPresenter;
import com.evento.team2.eventspack.soapservice.interfaces.ServiceEvento;
import com.evento.team2.eventspack.utils.NetworkUtils;
import com.evento.team2.eventspack.utils.interfaces.MainThread;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Daniel on 10-Jan-16.
 */
@Module
public class MainPresentersModule {

    @Provides
    @Singleton
    FragmentEventsPresenter provideFragmentEventsPresenter(EventiApplication application, PreferencesInteractor preferencesInteractor, MainThread mainThread,
                                                           DatabaseInteractor databaseInteractor, NetworkUtils networkUtils, ServiceEvento serviceEvento,
                                                           NotificationsInteractor notificationsInteractor) {
        return new FragmentEventsPresenterImpl(application, preferencesInteractor, mainThread, databaseInteractor, networkUtils, serviceEvento, notificationsInteractor);
    }

    @Provides
    @Singleton
    FragmentSavedEventsPresenterImpl provideFragmentSavedEventsPresenter(MainThread mainThread, DatabaseInteractor databaseInteractor,
                                                                    NotificationsInteractor notificationsInteractor) {
        return new FragmentSavedEventsPresenterImpl(mainThread, databaseInteractor, notificationsInteractor);
    }

    @Provides
    @Singleton
    FragmentCategoriesPresenter provideFragmentFragmentCategories(MainThread mainThread, DatabaseInteractor databaseInteractor) {
        return new FragmentCategoriesPresenterImpl(mainThread, databaseInteractor);
    }

    @Provides
    @Singleton
    FragmentPlacesPresenter provideFragmentPlacesPresenter(MainThread mainThread, DatabaseInteractor databaseInteractor) {
        return new FragmentPlacesPresenterImpl(mainThread, databaseInteractor);
    }
}