package com.evento.team2.eventspack.model;

/**
 * Created by Daniel on 04-Aug-15.
 */
public class Event {

    public String name;
    public String details;
    public String pictureUri;

    public Event(String name, String description) {
        this.name = name;
        this.details = description;
    }

    public Event(String name, String description, String pictureUri) {
        this.name = name;
        this.details = description;
        this.pictureUri = pictureUri;
    }
}
