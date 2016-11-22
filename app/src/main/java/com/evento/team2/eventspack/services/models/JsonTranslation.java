package com.evento.team2.eventspack.services.models;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by daniel-kareski on 8/8/16.
 */
@JsonObject
public class JsonTranslation {
    @JsonField
    public int code;
    @JsonField
    public String lang;
    @JsonField
    public String[] text;

    @Override
    public String toString() {
        return "JsonTranslation{" +
                "code=" + code +
                ", lang='" + lang + '\'' +
                ", text=" + Arrays.toString(text) +
                '}';
    }
}
