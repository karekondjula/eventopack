package com.evento.team2.eventspack.components;

import com.evento.team2.eventspack.modules.AppModule;
import com.evento.team2.eventspack.modules.PresentersModule;
import com.evento.team2.eventspack.presenters.interfaces.FragmentEventsPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Daniel on 15-Jan-16.
 */
@Singleton
@Component(modules = {AppModule.class, PresentersModule.class})
public interface InteractorsComponent {
    void inject(FragmentEventsPresenter fragmentEventsPresenter);
}
