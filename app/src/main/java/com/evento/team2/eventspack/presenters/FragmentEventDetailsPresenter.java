package com.evento.team2.eventspack.presenters;

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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Daniel on 06-Mar-16.
 */
public class FragmentEventDetailsPresenter {

    FragmentEventDetailsView eventDetailsView;
    MainThread mainThread;
    DatabaseInteractor databaseInteractor;
    private NotificationsInteractor notificationsInteractor;

    public FragmentEventDetailsPresenter(MainThread mainThread,
                                         DatabaseInteractor databaseInteractor,
                                         NotificationsInteractor notificationsInteractor) {
        this.mainThread = mainThread;
        this.databaseInteractor = databaseInteractor;
        this.notificationsInteractor = notificationsInteractor;
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

        eventDetailsView.notifyUserForUpdateInEvent(event.isEventSaved);
    }

    public void translateToEnglish(Event event) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TranslateService service = retrofit.create(TranslateService.class);

        Call<JsonTranslation> jsonTranslationCall = service.translate("mk-en",
                "trnsl.1.1.20160808T170832Z.353235d926160df6.e608b7d2675e0add16ee340b97cc50d80ff19416",
                event.details);
        jsonTranslationCall.enqueue(new Callback<JsonTranslation>() {
            @Override
            public void onResponse(Call<JsonTranslation> call, Response<JsonTranslation> response) {

                final JsonTranslation jsonTranslation = response.body();
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        eventDetailsView.setDetails(Arrays.toString(jsonTranslation.text));
                    }
                });
            }

            @Override
            public void onFailure(Call<JsonTranslation> call, Throwable t) {

            }
        });
    }
}