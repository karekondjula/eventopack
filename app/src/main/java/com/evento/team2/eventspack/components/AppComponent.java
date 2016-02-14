package com.evento.team2.eventspack.components;

/**
 * Created by Daniel on 16-Jan-16.
 */

import com.evento.team2.eventspack.modules.AppModule;
import com.evento.team2.eventspack.ui.fragments.FragmentCalendar;
import com.evento.team2.eventspack.ui.fragments.FragmentEvents;
import com.evento.team2.eventspack.ui.fragments.FragmentPlaces;
import com.evento.team2.eventspack.ui.fragments.FragmentSavedEvents;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(FragmentEvents fragmentEvents);
    void inject(FragmentPlaces fragmentPlaces);
    void inject(FragmentSavedEvents fragmentSavedEvents);
    void inject(FragmentCalendar fragmentCalendar);
}
