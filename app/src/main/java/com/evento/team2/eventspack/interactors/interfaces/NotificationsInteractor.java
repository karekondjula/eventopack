package com.evento.team2.eventspack.interactors.interfaces;

import com.evento.team2.eventspack.models.Event;

/**
 * Created by Daniel on 06-Mar-16.
 */
public interface NotificationsInteractor {

    void scheduleNotification(Event event);

    void removeScheduleNotification(Event event);
}
