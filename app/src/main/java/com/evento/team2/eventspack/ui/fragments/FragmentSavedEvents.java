package com.evento.team2.eventspack.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.adapters.EventsRecyclerViewAdapter;
import com.evento.team2.eventspack.components.AppComponent;
import com.evento.team2.eventspack.interactors.interfaces.NotificationsInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.presenters.interfaces.FragmentSavedEventsPresenter;
import com.evento.team2.eventspack.provider.FetchAsyncTask;
import com.evento.team2.eventspack.ui.fragments.interfaces.ObserverFragment;
import com.evento.team2.eventspack.views.FragmentSavedEventsView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 31-Jul-15.
 */
public class FragmentSavedEvents extends ObserverFragment implements FragmentSavedEventsView {

    @Inject
    FragmentSavedEventsPresenter fragmentSavedEventsPresenter;

    @Inject
    NotificationsInteractor notificationsInteractor;

    @Bind(R.id.savedEventsRecyclerView)
    RecyclerView savedEventsRecyclerView;

    private EventsRecyclerViewAdapter savedEventsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_saved_events, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        savedEventsAdapter = new EventsRecyclerViewAdapter(getActivity(), notificationsInteractor);
        savedEventsRecyclerView.setHasFixedSize(true);
        savedEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        savedEventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        savedEventsRecyclerView.setAdapter(savedEventsAdapter);

        fragmentSavedEventsPresenter.setView(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        fragmentSavedEventsPresenter.fetchSavedEvents(FetchAsyncTask.NO_FILTER_STRING);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static FragmentSavedEvents newInstance() {
        return new FragmentSavedEvents();
    }

    @Override
    public void filterList(final String filter) {
        fragmentSavedEventsPresenter.fetchSavedEvents(filter);
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @Override
    public void showSavedEvents(ArrayList<Event> eventsArrayList) {
        if (savedEventsAdapter != null) {
            savedEventsAdapter.addEvents((ArrayList<Event>) eventsArrayList);
            savedEventsAdapter.notifyDataSetChanged();
        }
    }
}