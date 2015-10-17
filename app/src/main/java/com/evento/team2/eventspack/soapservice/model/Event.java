package com.evento.team2.eventspack.soapservice.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Daniel on 10-Sep-15.
 */
@JsonObject
public class Event {

    @JsonField
    String calendar;
    @JsonField
    long id;
    @JsonField
    long pid;
    @JsonField
    long tstamp;
    @JsonField
    String title;
    @JsonField
    String alias;
    @JsonField
    String author;
    @JsonField
    long addTime;
    @JsonField
    long startTime;
    @JsonField
    long endTime;
    @JsonField
    long startDate;
    @JsonField
    long endDate;
    @JsonField
    String location;
    @JsonField
    String teaser;
    @JsonField
    String addImage;
    @JsonField
    String singleSRC;
    @JsonField
    String alt;
    @JsonField
    String size;
    @JsonField
    String imageMargin;
    @JsonField
    String imageUrl;
    @JsonField
    String fullsize;
    @JsonField
    String caption;
    @JsonField
    String floating;
    @JsonField
    String recurring;
    @JsonField
    int repeatEach;
    @JsonField
    int repeatEnd;
    @JsonField
    int recurrences;
    @JsonField
    String addEnclosure;
    @JsonField
    String enclosure;
    @JsonField
    String source;
    @JsonField
    int jumpTo;
    @JsonField
    int articleId;
    @JsonField
    String url;
    @JsonField
    String target;
    @JsonField
    String cssClass;
    @JsonField
    String noComments;
    @JsonField
    int published;
    @JsonField
    String start;
    @JsonField
    String stop;

//    @JsonField
//    double lat;
//    @JsonField
//    double lng;
//    @JsonField
//    String contact;
//    @JsonField
//    String website;
//    @JsonField
//    int price;
//    String address;
//    @JsonField
//    long id_user;
//    @JsonField
//    int is_premium;
//    @JsonField
//    long premium_date_from;
//    @JsonField
//    long premium_date_to;
}