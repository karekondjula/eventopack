package com.evento.team2.eventspack.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.adapters.PlacesRecyclerViewAdapter;
import com.evento.team2.eventspack.components.AppComponent;
import com.evento.team2.eventspack.models.Place;
import com.evento.team2.eventspack.presenters.interfaces.FragmentPlacesPresenter;
import com.evento.team2.eventspack.ui.fragments.interfaces.BaseFragment;
import com.evento.team2.eventspack.utils.EventiConstants;
import com.evento.team2.eventspack.views.FragmentPlacesView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 31-Jul-15.
 */
public class FragmentCategories extends BaseFragment {

//    @Inject
//    FragmentPlacesPresenter fragmentPlacesPresenter;

    @Bind(R.id.placesRecyclerView)
    RecyclerView placesRecyclerView;

    private PlacesRecyclerViewAdapter placesRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_places, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        placesRecyclerViewAdapter = new PlacesRecyclerViewAdapter(getActivity());
        placesRecyclerView.setHasFixedSize(true);
        placesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        placesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        placesRecyclerView.setAdapter(placesRecyclerViewAdapter);

//        fragmentPlacesPresenter.setView(this);
    }

    @Override
    public void onResume() {
        super.onResume();

//        fragmentPlacesPresenter.fetchPlaces(EventiConstants.NO_FILTER_STRING);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static FragmentCategories newInstance() {
        return new FragmentCategories();
    }

    @Override
    public void filterList(final String filter) {
//        fragmentPlacesPresenter.fetchPlaces(filter);
    }

    @Override
    protected void injectComponent(AppComponent component) {
//        component.inject(this);
    }
}