package com.evento.team2.eventspack.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.adapters.PlacesAdapter;
import com.evento.team2.eventspack.components.AppComponent;
import com.evento.team2.eventspack.models.Place;
import com.evento.team2.eventspack.presenters.interfaces.FragmentPlacesPresenter;
import com.evento.team2.eventspack.ui.fragments.interfaces.BaseFragment;
import com.evento.team2.eventspack.utils.EventiConstants;
import com.evento.team2.eventspack.views.FragmentPlacesView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 31-Jul-15.
 */
public class FragmentPlaces extends BaseFragment implements FragmentPlacesView {

    public static final String TAG = "FragmentPlaces";

    @Inject
    FragmentPlacesPresenter fragmentPlacesPresenter;

    @BindView(R.id.placesRecyclerView)
    RecyclerView placesRecyclerView;

    private PlacesAdapter placesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_places, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        placesAdapter = new PlacesAdapter(getActivity());
        placesRecyclerView.setHasFixedSize(true);
        placesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        placesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        placesRecyclerView.setAdapter(placesAdapter);

        fragmentPlacesPresenter.setView(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        fragmentPlacesPresenter.fetchPlaces(EventiConstants.NO_FILTER_STRING);
    }

    public static FragmentPlaces newInstance() {
        return new FragmentPlaces();
    }

    @Override
    public void filterList(final String filter) {
        fragmentPlacesPresenter.fetchPlaces(filter);
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @Override
    public void showPlaces(ArrayList<Place> placesArrayList) {
        if (placesAdapter != null) {
            placesAdapter.updateView(placesArrayList);
        }
    }
}