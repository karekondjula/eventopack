package com.evento.team2.eventspack.components;

import com.evento.team2.eventspack.modules.EventDetailsModule;
import com.evento.team2.eventspack.scopes.EventDetailsScope;
import com.evento.team2.eventspack.ui.activites.ActivityEventDetails;

import dagger.Component;

/**
 * Created by Daniel on 28-Feb-16.
 */
@EventDetailsScope
@Component(dependencies = AppComponent.class, modules = EventDetailsModule.class)
public interface EventDetailsComponent {
    // TODO change this to the fragment when created
    void inject(ActivityEventDetails activityEventDetails);
}
