package com.evento.team2.eventspack.presenters;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.interactors.interfaces.PreferencesInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.presenters.interfaces.FragmentEventsPresenter;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.evento.team2.eventspack.soapservice.ServiceEvento;
import com.evento.team2.eventspack.soapservice.model.JsonEvent;
import com.evento.team2.eventspack.ui.fragments.FragmentEvents;
import com.evento.team2.eventspack.utils.DateFormatterUtils;
import com.evento.team2.eventspack.utils.NetworkUtils;
import com.evento.team2.eventspack.utils.interfaces.MainThread;
import com.evento.team2.eventspack.views.FragmentEventsView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Daniel on 10-Jan-16.
 */
public class FragmentEventsPresenterImpl implements FragmentEventsPresenter {

    private final NetworkUtils networkUtils;
    private EventiApplication application;
    private FragmentEventsView fragmentEventsView;
    private MainThread mainThread;
    private DatabaseInteractor databaseInteractor;
    private PreferencesInteractor preferencesInteractor;

    private String lastQuery = "";

    public FragmentEventsPresenterImpl(EventiApplication application, PreferencesInteractor preferencesInteractor,
                                       MainThread mainThread, DatabaseInteractor databaseInteractor, NetworkUtils networkUtils) {
        this.application = application;
        this.preferencesInteractor = preferencesInteractor;
        this.mainThread = mainThread;
        this.databaseInteractor = databaseInteractor;
        this.networkUtils = networkUtils;
    }

    @Override
    public void setView(FragmentEventsView fragmentEventsView) {
        this.fragmentEventsView = fragmentEventsView;
    }

    @Override
    public void fetchEvents(String query) {

        new Thread() {
            @Override
            public void run() {
                final ArrayList<Event> eventArrayList = databaseInteractor.getActiveEvents(lastQuery);

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


        lastQuery = query;
    }

    @Override
    public void fetchEventsFromServer(boolean forceUpdate) {
        //TODO Maybe better to call it from alarmmanager two times a day ?!
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

//                        Observable<String> fetchFromGoogle = Observable.create(new Observable.OnSubscribe<List<Event>>() {
//                            @Override
//                            public void call(Subscriber<? super List<Event>> subscriber) {
//                                try {
//                                    subscriber.onNext(data); // Emit the contents of the URL
//                                    subscriber.onCompleted(); // Nothing more to emit
//                                }catch(Exception e){
//                                    subscriber.onError(e); // In case there are network errors
//                                }
//                            }
//                        });
//
//                        fetchFromGoogle
//                                .subscribeOn(Schedulers.newThread()) // Create a new Thread
//                                .observeOn(AndroidSchedulers.mainThread()) // Use the UI thread
//                                .subscribe(eventArrayList -> {
//                                    fragmentEventsView.showEvents(eventArrayList);
//                                });


                        HashMap<String, Object> params = new HashMap();
                        params.put(ServiceEvento.METHOD_NAME_KEY, ServiceEvento.METHOD_GET_ALL_EVENTS);
                        // TODO DAGGER
                        ServiceEvento.getInstance().callServiceMethod(params);

                        fetchEvents(lastQuery);

                        preferencesInteractor.setLastUpdateOfEvents(new Date().getTime());

                        mainThread.post(fragmentEventsView::stopRefreshAnimation);
                    } else {
                        mainThread.post(fragmentEventsView::showNoInternetConnectionMessage);
                    }
                }
            }
        }.start();
    }

    @Override
    public void fetchLastUpdatedTimestamp() {
//        String lastUpdateDate = "NOW";
        // TODO make it more human friendly
        long timestamp = preferencesInteractor.getLastUpdateOfEvents();
        String lastUpdateDate;
        if (timestamp == 0) {
            lastUpdateDate = application.getString(R.string.updating_now);
        } else {
            lastUpdateDate = DateFormatterUtils.fullDateFormat.format(timestamp);
        }

        fragmentEventsView.showLastUpdatedTimestampMessage(lastUpdateDate);
    }
}
