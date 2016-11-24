package com.evento.team2.eventspack.presenters;

import android.text.format.DateUtils;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.interactors.NotificationsInteractor;
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.interactors.interfaces.PreferencesInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.presenters.interfaces.FragmentEventsPresenter;
import com.evento.team2.eventspack.soapservice.interfaces.ServiceEvento;
import com.evento.team2.eventspack.utils.EventiConstants;
import com.evento.team2.eventspack.utils.NetworkUtils;
import com.evento.team2.eventspack.utils.interfaces.MainThread;
import com.evento.team2.eventspack.views.FragmentEventsView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Daniel on 10-Jan-16.
 */
public class FragmentEventsPresenterImpl implements FragmentEventsPresenter {

    NetworkUtils networkUtils;
    private EventiApplication application;
    FragmentEventsView fragmentEventsView;
    MainThread mainThread;
    DatabaseInteractor databaseInteractor;
    PreferencesInteractor preferencesInteractor;
    ServiceEvento serviceEvento;
    private NotificationsInteractor notificationsInteractor;

    String lastQuery = "";
    int lastOffset = EventiConstants.OFFSET;

    public FragmentEventsPresenterImpl(EventiApplication application, PreferencesInteractor preferencesInteractor, MainThread mainThread,
                                       DatabaseInteractor databaseInteractor, NetworkUtils networkUtils, ServiceEvento serviceEvento,
                                       NotificationsInteractor notificationsInteractor) {
        this.application = application;
        this.preferencesInteractor = preferencesInteractor;
        this.mainThread = mainThread;
        this.databaseInteractor = databaseInteractor;
        this.networkUtils = networkUtils;
        this.serviceEvento = serviceEvento;
        this.notificationsInteractor = notificationsInteractor;
    }

    @Override
    public void setView(FragmentEventsView fragmentEventsView) {
        this.fragmentEventsView = fragmentEventsView;
    }

    @Override
    public void fetchEvents(String query, int offset) {

        lastQuery = query;
        if (offset != 0) {
            lastOffset += offset;
        }
        new Thread() {
            @Override
            public void run() {
                final ArrayList<Event> eventArrayList = databaseInteractor.getActiveEvents(lastQuery, lastOffset);

                mainThread.post(() -> fragmentEventsView.showEvents(eventArrayList));
            }
        }.start();

//        Observable<String> eventsObservable = Observable.just(lastQuery);
//        eventsObservable
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
////                .flatMap(new Func1<List<Event>, Observable<Event>>() {
////                    @Override
////                    public Observable<Event> call(List<Event> events) {
////                        return Observable.from(events);
////                    }
////                })
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String lastQuery) {
//                        ArrayList<Event> eventArrayList = databaseInteractor.getActiveEvents(lastQuery);
//
//                        fragmentEventsView.showEvents(eventArrayList);
//                    }
//                });
    }

    @Override
    public void fetchEventsFromServer(boolean forceUpdate) {
        new Thread() {
            @Override
            public void run() {
                long lastUpdateDate = preferencesInteractor.getLastUpdateOfEvents();
                Calendar today, lastUpdateOfEvents;
                today = Calendar.getInstance();
                today.set(Calendar.HOUR_OF_DAY, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.SECOND, 0);
                today.set(Calendar.MILLISECOND, 0);

                lastUpdateOfEvents = Calendar.getInstance();
                lastUpdateOfEvents.setTimeInMillis(lastUpdateDate);
                lastUpdateOfEvents.set(Calendar.HOUR_OF_DAY, 0);
                lastUpdateOfEvents.set(Calendar.MINUTE, 0);
                lastUpdateOfEvents.set(Calendar.SECOND, 0);
                lastUpdateOfEvents.set(Calendar.MILLISECOND, 0);

                if (forceUpdate || today.getTimeInMillis() != lastUpdateOfEvents.getTimeInMillis()) {
                    if (networkUtils.isNetworkAvailable()) {

                        mainThread.post(fragmentEventsView::startRefreshAnimation);

                        // TODO this service implementation is idiotic ... Cthulu help us all
                        serviceEvento.getAllCurrentEvents();

                        fetchEvents(lastQuery, lastOffset);

                        preferencesInteractor.setLastUpdateOfEvents(new Date().getTime());
                    } else {
                        mainThread.post(fragmentEventsView::showNoInternetConnectionMessage);
                    }

                    mainThread.post(fragmentEventsView::stopRefreshAnimation);
                }
            }
        }.start();
    }

    @Override
    public void fetchLastUpdatedTimestamp() {
        long timestamp = preferencesInteractor.getLastUpdateOfEvents();
        String lastUpdateDate;

        if (timestamp == 0) {
            lastUpdateDate = application.getString(R.string.updating_now);
        } else {
            lastUpdateDate = DateUtils.getRelativeDateTimeString(application, timestamp,
                    DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0).toString();
        }

        fragmentEventsView.showLastUpdatedTimestampMessage(lastUpdateDate);
    }

    @Override
    public void fetchSavedEvents(String query) {
        new Thread() {
            @Override
            public void run() {
                ArrayList<Event> savedEventsArrayList = databaseInteractor.getSavedEvents(query);
                mainThread.post(() -> fragmentEventsView.showEvents(savedEventsArrayList));
            }
        }.start();
    }

    @Override
    public void changeSavedStateOfEvent(Event event) {
        event.isEventSaved = !event.isEventSaved;

        databaseInteractor.changeSaveEvent(event, event.isEventSaved);

        if (event.isEventSaved) {
            notificationsInteractor.scheduleNotification(event);
        } else {
            notificationsInteractor.removeScheduleNotification(event);
        }

        fragmentEventsView.notifyUserForUpdateInEvent(event.isEventSaved, event.name);
    }
}