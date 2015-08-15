package com.evento.team2.eventspack.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

/**
 * Created by Daniel on 04-Aug-15.
 */
public class Event {

    public String name;
    public String details;
    public String pictureUri;
    public LatLng position;

    public Event(String name, String description) {
        this.name = name;
        this.details = description;

        position = new LatLng(new Random().nextInt(90) * (new Random().nextBoolean() ? 1 : -1),
                new Random().nextInt(180) * (new Random().nextBoolean() ? 1 : -1));
    }

    public Event(String name, String description, String pictureUri) {
        this.name = name;
        this.details = description;
        this.pictureUri = pictureUri;
    }
}
