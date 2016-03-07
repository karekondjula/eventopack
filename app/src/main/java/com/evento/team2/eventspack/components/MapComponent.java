package com.evento.team2.eventspack.components;

import com.evento.team2.eventspack.modules.CalendarModule;
import com.evento.team2.eventspack.modules.MapModule;
import com.evento.team2.eventspack.scopes.CalendarScope;
import com.evento.team2.eventspack.scopes.MapScope;
import com.evento.team2.eventspack.ui.fragments.FragmentCalendar;
import com.evento.team2.eventspack.ui.fragments.FragmentMap;

import dagger.Component;

/**
 * Created by Daniel on 28-Feb-16.
 */
@MapScope
@Component(dependencies = AppComponent.class, modules = MapModule.class)
public interface MapComponent {
    void inject(FragmentMap fragmentMap);
}
