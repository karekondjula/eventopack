package com.evento.team2.eventspack.ui.fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.adapter.EventsRecyclerViewAdapter;
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.soapservice.ServiceEvento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;

//import com.joanzapata.iconify.Iconify;
//import com.joanzapata.iconify.fonts.IoniconsModule;
//import rx.Observable;
//import rx.Subscription;

/**
 * Created by Daniel on 31-Jul-15.
 */
public class FragmentEvents extends Fragment implements Observer{

    @Bind(R.id.eventsRecyclerView)
    RecyclerView eventsRecyclerView;

    private EventsRecyclerViewAdapter eventsAdapter;

    private boolean updateFromServerArrived = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_events_list, container, false);
        ButterKnife.bind(this, swipeRefreshLayout);
//        Iconify.with(new IoniconsModule());

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Refresh items

            new Thread() {
                @Override
                public void run() {
                    updateFromServerArrived = false;

                    HashMap<String, Object> params = new HashMap();
                    params.put(ServiceEvento.METHOD_NAME_KEY, ServiceEvento.METHOD_GET_ALL_EVENTS);
                    ServiceEvento.getInstance().callServiceMethod(params);

                    while (!updateFromServerArrived) {
                        SystemClock.sleep(100);
                    }

                    getActivity().runOnUiThread(() -> {
//                                eventsAdapter.notifyItemRangeInserted(0, 6);
//                        eventsAdapter.notifyItemInserted(0);

                        swipeRefreshLayout.setRefreshing(false);

//                        if (((LinearLayoutManager) eventsRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0) {
//                            eventsRecyclerView.getLayoutManager().scrollToPosition(0);
//                        }
                    });

                }
            }.start();
        });

        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        eventsAdapter = new EventsRecyclerViewAdapter(getActivity());
        eventsRecyclerView.setAdapter(eventsAdapter);

        HashMap<String, Object> params = new HashMap();
        params.put(ServiceEvento.METHOD_NAME_KEY, ServiceEvento.METHOD_GET_ALL_EVENTS);
        ServiceEvento.getInstance().callServiceMethod(params);

        return swipeRefreshLayout;
    }

    @Override
    public void onDestroy() {
        // TODO daniel check whether we need addObserver in OnREsume and the other
        // TODO lifecycle methods
        ServiceEvento.getInstance().deleteObserver(this);
        super.onDestroy();
    }

    public static FragmentEvents newInstance() {
        FragmentEvents fragmentEvents = new FragmentEvents();
        ServiceEvento.getInstance().addObserver(fragmentEvents);
        return fragmentEvents;
    }

    @Override
    public void update(Observable observable, Object eventsArrayList) {

        if (eventsArrayList instanceof ArrayList) {
            updateFromServerArrived = true;
            eventsAdapter.addEvents((ArrayList<Event>) eventsArrayList);
            eventsAdapter.notifyDataSetChanged();
        }
    }
}


//    Subscription newEventsSubscription;
//
//    private Subscription subscribeOnEventsObservable() {
//        Observable<Event> eventsObservable = EventsObservable.getObservableBackgroundThread();
//
//        return eventsObservable.subscribe(sub -> eventsAdapter.addEvent(sub),
//                err -> {},
//                () -> {
//                    eventsAdapter = new EventsRecyclerViewAdapter(getActivity(), eventsAdapter.getEventsList());
//                    eventsRecyclerView.setAdapter(eventsAdapter);
//                });
//    }
