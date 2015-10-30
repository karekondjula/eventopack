package com.evento.team2.eventspack.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.adapter.EventsRecyclerViewAdapter;
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.evento.team2.eventspack.ui.interfaces.ObserverFragment;

import java.util.ArrayList;
import java.util.Observable;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 31-Jul-15.
 */
public class FragmentSavedEvents extends ObserverFragment {

    @Bind(R.id.savedEventsRecyclerView)
    RecyclerView savedEventsRecyclerView;

    private EventsRecyclerViewAdapter savedEventsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_events, container, false);
        ButterKnife.bind(this, view);

        savedEventsAdapter = new EventsRecyclerViewAdapter(getActivity());
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        savedEventsRecyclerView.setHasFixedSize(true);
        savedEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        savedEventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        savedEventsRecyclerView.setAdapter(savedEventsAdapter);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

//    public void onEvent(EventsController.UpdateSavedEvents updateEvents) {
//
//        Log.i(">>", "UpdateSavedEvents");
//
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                ArrayList<JsonEvent> savedEvents = EventsDatabase.getInstance().getEvents();
//                savedEventsAdapter.refreshEvents(savedEvents);
////                savedEventsAdapter.notifyItemRangeChanged(0, savedEvents.size());
//                savedEventsAdapter.notifyDataSetChanged();
////        savedEventsAdapter.notifyItemInserted(0);
////                savedEventsRecyclerView.setAdapter(savedEventsAdapter);
//            }
//        });
//    };

    public static FragmentSavedEvents newInstance() {
        FragmentSavedEvents f = new FragmentSavedEvents();
        return f;
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}