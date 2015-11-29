package com.evento.team2.eventspack.ui.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.adapter.PlacesRecyclerViewAdapter;
import com.evento.team2.eventspack.model.Place;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.evento.team2.eventspack.provider.FetchAsyncTask;
import com.evento.team2.eventspack.ui.interfaces.ObserverFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 31-Jul-15.
 */
public class FragmentPlaces extends ObserverFragment {

    @Bind(R.id.placesRecyclerView)
    RecyclerView placesRecyclerView;

    private volatile PlacesRecyclerViewAdapter placesRecyclerViewAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        placesRecyclerViewAdapter = new PlacesRecyclerViewAdapter(EventiApplication.applicationContext);
        placesRecyclerView.setHasFixedSize(true);
        placesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        placesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        placesRecyclerView.setAdapter(placesRecyclerViewAdapter);

        filterList(FetchAsyncTask.NO_FILTER_STRING);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_places, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (fetchAsyncTask != null && fetchAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            fetchAsyncTask.cancel(true);
        }
    }

    public static FragmentPlaces newInstance() {
        FragmentPlaces f = new FragmentPlaces();
        return f;
    }

    @Override
    public void filterList(final String filter) {
        new Thread() {
            @Override
            public void run() {

                ArrayList<Place> eventsArrayList;
                if (filter != null) {
                    eventsArrayList = EventsDatabase.getInstance().getPlaces(filter);
                } else {
                    eventsArrayList = EventsDatabase.getInstance().getPlaces();
                }

                if (placesRecyclerViewAdapter != null) {
                    placesRecyclerViewAdapter.addPlaces(eventsArrayList);
                    getActivity().runOnUiThread(() -> placesRecyclerViewAdapter.notifyDataSetChanged());
                }
            }
        }.start();
    }
}