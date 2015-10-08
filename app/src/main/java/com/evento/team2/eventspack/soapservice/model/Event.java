package com.evento.team2.eventspack.soapservice.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Daniel on 10-Sep-15.
 */
@JsonObject
public class Event {

    @JsonField
    long id;
    @JsonField
    String name;
    @JsonField
    String description;
    @JsonField
    long date_from;
    @JsonField
    long date_to;
    @JsonField
    double lat;
    @JsonField
    double lng;
    @JsonField
    String image_url;
    @JsonField
    String contact;
    @JsonField
    String website;
    @JsonField
    int price;
    String address;
    @JsonField
    long id_user;
    @JsonField
    int published;
    @JsonField
    int is_premium;
    @JsonField
    long premium_date_from;
    @JsonField
    long premium_date_to;
}

