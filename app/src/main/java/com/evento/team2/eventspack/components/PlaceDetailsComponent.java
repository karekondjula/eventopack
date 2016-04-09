package com.evento.team2.eventspack.components;

import com.evento.team2.eventspack.modules.EventDetailsModule;
import com.evento.team2.eventspack.modules.PlaceDetailsModule;
import com.evento.team2.eventspack.scopes.EventDetailsScope;
import com.evento.team2.eventspack.scopes.PlaceDetailsScope;
import com.evento.team2.eventspack.ui.activites.ActivityEventDetails;
import com.evento.team2.eventspack.ui.activites.ActivityPlaceDetails;

import dagger.Component;

/**
 * Created by Daniel on 28-Feb-16.
 */
@PlaceDetailsScope
@Component(dependencies = AppComponent.class, modules = PlaceDetailsModule.class)
public interface PlaceDetailsComponent {
    // TODO change this to the fragment when created
    void inject(ActivityPlaceDetails activityPlaceDetails);
}
