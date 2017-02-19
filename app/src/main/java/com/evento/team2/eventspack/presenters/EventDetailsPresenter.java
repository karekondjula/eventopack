package com.evento.team2.eventspack.presenters;

import android.text.TextUtils;

import com.evento.team2.eventspack.interactors.NotificationsInteractor;
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.services.TranslateService;
import com.evento.team2.eventspack.services.models.JsonTranslation;
import com.evento.team2.eventspack.utils.interfaces.MainThread;
import com.evento.team2.eventspack.views.FragmentEventDetailsView;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Daniel on 06-Mar-16.
 */
public class EventDetailsPresenter {

    FragmentEventDetailsView eventDetailsView;
    MainThread mainThread;
    DatabaseInteractor databaseInteractor;
    private NotificationsInteractor notificationsInteractor;
    TranslateService translateService;
    String translatedDetails;

    public EventDetailsPresenter(MainThread mainThread, DatabaseInteractor databaseInteractor,
                                 NotificationsInteractor notificationsInteractor, TranslateService translateService) {
        this.mainThread = mainThread;
        this.databaseInteractor = databaseInteractor;
        this.notificationsInteractor = notificationsInteractor;
        this.translateService = translateService;
    }

    public void setView(FragmentEventDetailsView fragmentEventDetailsView) {
        this.eventDetailsView = fragmentEventDetailsView;
    }

    public void fetchEventDetails(long eventId) {
        new Thread() {
            @Override
            public void run() {
                final Event event = databaseInteractor.getEventById(eventId);
                mainThread.post(() -> eventDetailsView.showEvent(event));
            }
        }.start();
    }

    public void changeSavedStateOfEvent(Event event) {
        event.isEventSaved = !event.isEventSaved;

        databaseInteractor.changeSaveEvent(event, event.isEventSaved);

        if (event.isEventSaved) {
            notificationsInteractor.scheduleNotification(event);
        } else {
            notificationsInteractor.removeScheduleNotification(event);
        }

        eventDetailsView.notifyUserForSavedEvent(event.isEventSaved);
    }

    public void translateToEnglish(Event event) {

        eventDetailsView.showTranslatingMessage();

//        Call<JsonDetection> jsonDetect = service.detect(
//                "trnsl.1.1.20160808T170832Z.353235d926160df6.e608b7d2675e0add16ee340b97cc50d80ff19416",
//                event.details);
//        jsonDetect.enqueue(new Callback<JsonDetection>() {
//            @Override
//            public void onResponse(Call<JsonDetection> call, Response<JsonDetection> response) {
//
//                final JsonDetection jsonTranslation = response.body();
//                final boolean isResponseGood = response.isSuccessful();
//                mainThread.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (isResponseGood) {
//                            Log.d(">>", jsonTranslation.lang);
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(Call<JsonDetection> call, Throwable t) {
//                // TODO inform user
//            }
//        });

        if (!TextUtils.isEmpty(translatedDetails)) {
            eventDetailsView.setTranslatedDetails(translatedDetails);
            eventDetailsView.dismissTranslatingMessage();
        } else {
            Call<JsonTranslation> jsonTranslationCall = translateService.translate("mk-en",
                    "trnsl.1.1.20170217T010551Z.374758616c73478a.0c1422580b92a08b63f38a67b3e601507f8388b5",
                    event.details);
            jsonTranslationCall.enqueue(new Callback<JsonTranslation>() {
                @Override
                public void onResponse(Call<JsonTranslation> call, Response<JsonTranslation> response) {

                    final JsonTranslation jsonTranslation = response.body();
                    final boolean isResponseGood = response.isSuccessful();
                    mainThread.post(new Runnable() {
                        @Override
                        public void run() {
                            if (isResponseGood) {
                                translatedDetails = Arrays.toString(jsonTranslation.text);
                                translatedDetails = translatedDetails.substring(1, translatedDetails.length() - 2);

                                eventDetailsView.setTranslatedDetails(translatedDetails);

                                eventDetailsView.dismissTranslatingMessage();
                            }
                        }
                    });
                }

                @Override
                public void onFailure(Call<JsonTranslation> call, Throwable t) {
                    // TODO inform user
                }
            });
        }
    }
}