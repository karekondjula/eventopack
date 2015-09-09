package com.evento.team2.eventspack.soapservice.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Daniel on 19-Aug-15.
 */
@JsonObject
public class User {
    @JsonField(name = "tstamp")
    public long timestamp;
    @JsonField(name = "username")
    public String username;
    @JsonField(name = "name")
    public String name;
    @JsonField(name = "email")
    public String email;
    @JsonField(name = "password")
    public String password;
}
