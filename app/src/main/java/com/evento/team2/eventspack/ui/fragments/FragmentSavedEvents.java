package com.evento.team2.eventspack.ui.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evento.team2.eventspack.EventiApplication;
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
    public void update(Observable observable, Object eventsArrayList) {
        if (eventsArrayList instanceof ArrayList) {
            if (savedEventsAdapter != null) {
                // TODO ugly solution for a problem which is caused because I use
                // TODO one fetchasync task for all data fetching
                savedEventsAdapter.addEvents((ArrayList<Event>) eventsArrayList);
                savedEventsAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void filterList(String filter) {
        fetchAsyncTask = new FetchAsyncTask(this, FetchAsyncTask.SAVED_EVENTS, FetchAsyncTask.DO_NOT_FETCH_FROM_SERVER);
        if (TextUtils.isEmpty(filter)) {
            fetchAsyncTask.execute();
        } else {
            fetchAsyncTask.execute(filter);
        }
    }
}