package com.evento.team2.eventspack.modules;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.interactors.interfaces.PreferencesInteractor;
import com.evento.team2.eventspack.presenters.FragmentCalendarPresenterImpl;
import com.evento.team2.eventspack.presenters.FragmentEventsPresenterImpl;
import com.evento.team2.eventspack.presenters.FragmentPlacesPresenterImpl;
import com.evento.team2.eventspack.presenters.FragmentSavedEventsPresenterImpl;
import com.evento.team2.eventspack.presenters.interfaces.FragmentCalendarPresenter;
import com.evento.team2.eventspack.presenters.interfaces.FragmentEventsPresenter;
import com.evento.team2.eventspack.presenters.interfaces.FragmentPlacesPresenter;
import com.evento.team2.eventspack.presenters.interfaces.FragmentSavedEventsPresenter;
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
                                                           DatabaseInteractor databaseInteractor, NetworkUtils networkUtils) {
        return new FragmentEventsPresenterImpl(application, preferencesInteractor, mainThread, databaseInteractor, networkUtils);
    }

    @Provides
    @Singleton
    FragmentPlacesPresenter provideFragmentPlacesPresenter(MainThread mainThread, DatabaseInteractor databaseInteractor) {
        return new FragmentPlacesPresenterImpl(mainThread, databaseInteractor);
    }

    @Provides
    @Singleton
    FragmentSavedEventsPresenter provideFragmentSavedEventsPresenter(MainThread mainThread, DatabaseInteractor databaseInteractor) {
        return new FragmentSavedEventsPresenterImpl(mainThread, databaseInteractor);
    }
}