package com.evento.team2.eventspack.utils;

import de.greenrobot.event.EventBus;

/**
 * Created by Daniel on 16-Aug-15.
 */
public class EventsController {

    /**
     * An events message used with the events framework for notifying changes
     */
    public static class UpdateEvents {}

    /**
     * Update the saved events list
     */
    public static class UpdateSavedEvents {}

    private static EventsController instance;

    private EventBus eventBus;

    private EventsController() {
        eventBus = new EventBus();
    }

    public static EventsController getInstance() {
        if (instance == null) {
            instance = new EventsController();
        }

        return instance;
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}
