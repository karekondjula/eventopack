package com.evento.team2.eventspack.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.adapters.EventsAdapter;
import com.evento.team2.eventspack.components.AppComponent;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.presenters.FragmentSavedEventsPresenterImpl;
import com.evento.team2.eventspack.ui.fragments.interfaces.BaseFragment;
import com.evento.team2.eventspack.utils.EventiConstants;
import com.evento.team2.eventspack.views.FragmentEventsView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 31-Jul-15.
 */
public class FragmentSavedEvents extends BaseFragment implements FragmentEventsView {

    @Inject
    FragmentSavedEventsPresenterImpl fragmentEventsPresenter;

    @BindView(R.id.savedEventsRecyclerView)
    RecyclerView savedEventsRecyclerView;

    private EventsAdapter savedEventsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_saved_events, container, false);
        unbinder = ButterKnife.bind(this, view);

        savedEventsRecyclerView.setHasFixedSize(true);
        savedEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        savedEventsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        savedEventsAdapter = new EventsAdapter(getActivity(), fragmentEventsPresenter);
        savedEventsRecyclerView.setAdapter(savedEventsAdapter);
        fragmentEventsPresenter.setView(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        fragmentEventsPresenter.fetchSavedEvents(EventiConstants.NO_FILTER_STRING);
    }

    public static FragmentSavedEvents newInstance() {
        return new FragmentSavedEvents();
    }

    @Override
    public void filterList(final String filter) {
        fragmentEventsPresenter.fetchSavedEvents(filter);
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @Override
    public void showEvents(ArrayList<Event> eventsArrayList) {
        if (savedEventsAdapter != null) {
            savedEventsAdapter.addEvents(eventsArrayList);
        }
    }

    @Override
    public void startRefreshAnimation() {

    }

    @Override
    public void stopRefreshAnimation() {

    }

    @Override
    public void showLastUpdatedTimestampMessage(String lastUpdateTimestamp) {

    }

    @Override
    public void showNoInternetConnectionMessage() {

    }

    @Override
    public void notifyUserForUpdateInEvent(boolean isSaved, String eventName) {
        Snackbar.make(savedEventsRecyclerView,
                isSaved ?
                        String.format(getResources().getString(R.string.event_is_saved), eventName) :
                        String.format(getResources().getString(R.string.event_is_removed), eventName),
                Snackbar.LENGTH_LONG)
                .show();
    }
}