package com.evento.team2.eventspack.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.adapter.EventsRecyclerViewAdapter;
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.evento.team2.eventspack.provider.FetchAsyncTask;
import com.evento.team2.eventspack.ui.interfaces.ObserverFragment;

import java.util.ArrayList;

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        savedEventsAdapter = new EventsRecyclerViewAdapter(EventiApplication.applicationContext);
        savedEventsRecyclerView.setHasFixedSize(true);
        savedEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        savedEventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        savedEventsRecyclerView.setAdapter(savedEventsAdapter);

        filterList(FetchAsyncTask.NO_FILTER_STRING);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_saved_events, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static FragmentSavedEvents newInstance() {
        FragmentSavedEvents f = new FragmentSavedEvents();
        return f;
    }

    @Override
    public void filterList(final String filter) {
        new Thread() {
            @Override
            public void run() {

                ArrayList<Event> eventsArrayList;
                if (filter != null) {
                    eventsArrayList = EventsDatabase.getInstance().getSavedEvents(filter);
                } else {
                    eventsArrayList = EventsDatabase.getInstance().getSavedEvents();
                }

                if (savedEventsAdapter != null) {
                    savedEventsAdapter.addEvents((ArrayList<Event>) eventsArrayList);
                    getActivity().runOnUiThread(() -> savedEventsAdapter.notifyDataSetChanged());
                }
            }
        }.start();
    }
}