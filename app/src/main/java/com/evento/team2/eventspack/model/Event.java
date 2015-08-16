package com.evento.team2.eventspack.model;

import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

/**
 * Created by Daniel on 04-Aug-15.
 */
public class Event {

    /**
     * Universal id for the event, same on all devices
     */
    public long id;
    public String name;
    public String details;
    public String pictureUri;
    public LatLng location;
    public boolean isEventSaved;
    public long date;
    public String dateString;

    public Event(String name, String description) {
        this.name = name;
        this.details = description;
    }

    public Event(String name, String description, @Nullable String pictureUri) {
        this.name = name;
        this.details = description;
        this.pictureUri = pictureUri;
    }
}