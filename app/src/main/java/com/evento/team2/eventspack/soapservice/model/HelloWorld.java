package com.evento.team2.eventspack.soapservice.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Daniel on 18-Aug-15.
 */
@JsonObject
public class HelloWorld {

    @JsonField
    public String message;

    @Override
    public String toString() {
        return message;
    }
}
