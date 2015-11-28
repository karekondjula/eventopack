package com.evento.team2.eventspack.ui.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evento.team2.eventspack.EventiApplication;
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
    public void update(Observable observable, Object placesArrayList) {
        if (placesArrayList instanceof ArrayList) {
            if (placesRecyclerViewAdapter != null) {
                // TODO ugly solution for a problem which is caused because I use
                // TODO one fetchasync task for all data fetching
                placesRecyclerViewAdapter.addPlaces((ArrayList<Place>) placesArrayList);
                placesRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void filterList(String filter) {
        fetchAsyncTask = new FetchAsyncTask(this, FetchAsyncTask.PLACES, FetchAsyncTask.DO_NOT_FETCH_FROM_SERVER);
        if (TextUtils.isEmpty(filter)) {
            fetchAsyncTask.execute();
        } else {
            fetchAsyncTask.execute(filter);
        }
    }
}