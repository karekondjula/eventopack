package com.evento.team2.eventspack.soapservice;

import com.evento.team2.eventspack.model.Event;

import java.util.HashMap;

//import rx.Observable;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;

/**
 * Created by Daniel on 18-Oct-15.
 */
public class EventsObservable {

//    public static Observable<Event> getObservable() {
//        return Observable.create(sub -> {
//                    try {
//                        HashMap<String, Object> params = new HashMap<>();
//                        params.put(ServiceEvento.METHOD_NAME_KEY, ServiceEvento.METHOD_GET_ALL_EVENTS);
//
//                        ServiceEvento.getInstance().callServiceMethod(params);
////                        sub.onNext("!!!");
////                        sub.onCompleted();
//                    } catch (Exception e) {
//                        sub.onError(e);
//                    }
//                }
//        );
//    }
//
//    public static Observable<Event> getObservableBackgroundThread() {
//        return getObservable()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
}
