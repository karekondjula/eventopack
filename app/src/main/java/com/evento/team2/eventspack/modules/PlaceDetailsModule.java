package com.evento.team2.eventspack.modules;

import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.presenters.FragmentEventDetailsPresenterImpl;
import com.evento.team2.eventspack.presenters.FragmentPlaceDetailsPresenterImpl;
import com.evento.team2.eventspack.presenters.interfaces.FragmentEventDetailsPresenter;
import com.evento.team2.eventspack.presenters.interfaces.FragmentPlaceDetailsPresenter;
import com.evento.team2.eventspack.scopes.EventDetailsScope;
import com.evento.team2.eventspack.scopes.PlaceDetailsScope;
import com.evento.team2.eventspack.utils.interfaces.MainThread;
import com.evento.team2.eventspack.views.FragmentPlaceDetailsView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Daniel on 06-Mar-16.
 */
@Module
public class PlaceDetailsModule {

    @Provides
    @PlaceDetailsScope
    FragmentPlaceDetailsPresenter provideFragmentPlaceDetailsPresenter(MainThread mainThread, DatabaseInteractor databaseInteractor) {
        return new FragmentPlaceDetailsPresenterImpl(mainThread, databaseInteractor);
    }
}
