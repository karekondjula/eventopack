package com.evento.team2.eventspack.services;

import com.evento.team2.eventspack.services.models.JsonDetection;
import com.evento.team2.eventspack.services.models.JsonTranslation;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by daniel-kareski on 8/8/16.
 */
public interface TranslateService {

    // https://translate.yandex.net/api/v1.5/tr.json
    @POST("/api/v1.5/tr.json/translate")
    Call<JsonTranslation> translate(
            @Query("lang") String lang,
            @Query("key") String apiKey,
            @Query("text") String textToTranslate
    );

    // https://translate.yandex.net/api/v1.5/detect
    @POST("/api/v1.5/detect")
    Call<JsonDetection> detect(
            @Query("key") String apiKey,
            @Query("text") String textToTranslate
    );
}
