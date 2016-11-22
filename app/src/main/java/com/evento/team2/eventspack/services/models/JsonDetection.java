package com.evento.team2.eventspack.services.models;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by daniel-kareski on 8/8/16.
 */
@JsonObject
public class JsonDetection {
    @JsonField
    public int code;
    @JsonField
    public String lang;

    @Override
    public String toString() {
        return "JsonTranslation{" +
                "code=" + code +
                ", lang='" + lang + '\'' +
                '}';
    }
}
