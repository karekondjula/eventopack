package com.evento.team2.eventspack.modules;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.interactors.interfaces.PreferencesInteractor;
import com.evento.team2.eventspack.presenters.FragmentEventsPresenterImpl;
import com.evento.team2.eventspack.presenters.interfaces.FragmentEventsPresenter;
import com.evento.team2.eventspack.utils.interfaces.MainThread;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Daniel on 10-Jan-16.
 */
@Module
public class PresentersModule {

    @Provides
    @Singleton
    FragmentEventsPresenter provideFragmentEventsPresenter(EventiApplication application, PreferencesInteractor preferencesInteractor, MainThread mainThread) {
        return new FragmentEventsPresenterImpl(application, preferencesInteractor, mainThread);
    }

//    @Provides
//    @Singleton
//    FragmentEventsPresenterImpl provideFragmentPlacesPresenter(EventiApplication application) {
//        return new FragmentEventsPresenterImpl(application);
//    }

}
