package com.evento.team2.eventspack.components;

import com.evento.team2.eventspack.modules.AppModule;
import com.evento.team2.eventspack.modules.CalendarModule;
import com.evento.team2.eventspack.scopes.CalendarScope;
import com.evento.team2.eventspack.ui.fragments.FragmentCalendar;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Daniel on 28-Feb-16.
 */
@CalendarScope
@Component(dependencies = AppComponent.class, modules = CalendarModule.class)
public interface CalendarComponent {
    void inject(FragmentCalendar fragmentCalendar);
}
