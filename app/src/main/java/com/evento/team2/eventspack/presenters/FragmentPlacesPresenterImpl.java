package com.evento.team2.eventspack.presenters;

import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.models.Place;
import com.evento.team2.eventspack.presenters.interfaces.FragmentPlacesPresenter;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.evento.team2.eventspack.utils.interfaces.MainThread;
import com.evento.team2.eventspack.views.FragmentPlacesView;

import java.util.ArrayList;

/**
 * Created by Daniel on 10-Jan-16.
 */
public class FragmentPlacesPresenterImpl implements FragmentPlacesPresenter {

    private FragmentPlacesView fragmentPlacesView;
    private MainThread mainThread;
    private DatabaseInteractor databaseInteractor;

    public FragmentPlacesPresenterImpl(MainThread mainThread, DatabaseInteractor databaseInteractor) {
        this.mainThread = mainThread;
        this.databaseInteractor = databaseInteractor;
    }

    @Override
    public void setView(FragmentPlacesView fragmentEventsView) {
        this.fragmentPlacesView = fragmentEventsView;
    }

    @Override
    public void fetchPlaces(String query) {
        new Thread() {
            @Override
            public void run() {

                final ArrayList<Place> placeArrayList = databaseInteractor.getPlaces(query);
                mainThread.post(() -> fragmentPlacesView.showPlaces(placeArrayList));
            }
        }.start();

//        Observable<String> eventsObservable = Observable.just(query);
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
//                        ArrayList<Place> placeArrayList = EventsDatabase.getInstance().getPlaces(query);
//
//                        fragmentPlacesView.showPlaces(placeArrayList);
//                    }
//                });
    }
}