package com.evento.team2.eventspack.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.adapter.PlacesRecyclerViewAdapter;
import com.evento.team2.eventspack.model.Place;
import com.evento.team2.eventspack.provider.FetchAsyncTask;
import com.evento.team2.eventspack.ui.interfaces.ObserverFragment;

import java.util.ArrayList;
import java.util.Observable;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 31-Jul-15.
 */
public class FragmentPlaces extends ObserverFragment {

    @Bind(R.id.placesRecyclerView)
    RecyclerView placesRecyclerView;

    private PlacesRecyclerViewAdapter placesRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_places, container, false);
        ButterKnife.bind(this, view);

        placesRecyclerViewAdapter = new PlacesRecyclerViewAdapter(getActivity());
        placesRecyclerView.setHasFixedSize(true);
        placesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        placesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        placesRecyclerView.setAdapter(placesRecyclerViewAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchAsyncTask = new FetchAsyncTask(this, FetchAsyncTask.PLACES, FetchAsyncTask.DO_NOT_FETCH_FROM_SERVER);
        fetchAsyncTask.execute();
    }

    @Override
    public void onDetach() {
        ButterKnife.unbind(this);
        super.onDetach();
    }

    public static FragmentPlaces newInstance() {
        FragmentPlaces f = new FragmentPlaces();
        return f;
    }

    @Override
    public void update(Observable observable, Object placesArrayList) {
        if (placesArrayList instanceof ArrayList) {
            placesRecyclerViewAdapter.addEvents((ArrayList<Place>) placesArrayList);
            placesRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void filterEvents(String filter) {
        fetchAsyncTask = new FetchAsyncTask(this, FetchAsyncTask.PLACES, FetchAsyncTask.DO_NOT_FETCH_FROM_SERVER);
        fetchAsyncTask.execute(FetchAsyncTask.FILTER_NAME, filter);
    }
}