package com.evento.team2.eventspack.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.adapter.EventsRecyclerViewAdapter;
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.provider.FetchAsyncTask;
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

    // TODO search was not functional
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_saved_events, container, false);
        ButterKnife.bind(this, view);

        savedEventsAdapter = new EventsRecyclerViewAdapter(getActivity());
        savedEventsRecyclerView.setHasFixedSize(true);
        savedEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        savedEventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        savedEventsRecyclerView.setAdapter(savedEventsAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new FetchAsyncTask(this, FetchAsyncTask.SAVED_EVENTS, FetchAsyncTask.DO_NOT_FETCH_FROM_SERVER).execute();
    }

    @Override
    public void onDetach() {
        ButterKnife.unbind(this);
        super.onDetach();
    }

    public static FragmentSavedEvents newInstance() {
        FragmentSavedEvents f = new FragmentSavedEvents();
        return f;
    }

    @Override
    public void update(Observable observable, Object eventsArrayList) {
        if (eventsArrayList instanceof ArrayList) {
            savedEventsAdapter.addEvents((ArrayList<Event>) eventsArrayList);
            savedEventsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void filterEvents(String filter) {
        new FetchAsyncTask(this, FetchAsyncTask.SAVED_EVENTS, FetchAsyncTask.DO_NOT_FETCH_FROM_SERVER).execute(FetchAsyncTask.FILTER_NAME, filter);
    }
}