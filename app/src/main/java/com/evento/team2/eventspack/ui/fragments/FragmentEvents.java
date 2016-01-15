package com.evento.team2.eventspack.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.adapters.EventsRecyclerViewAdapter;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.presenters.interfaces.FragmentEventsPresenter;
import com.evento.team2.eventspack.ui.fragments.interfaces.ObserverFragment;
import com.evento.team2.eventspack.views.FragmentEventsView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 31-Jul-15.
 */
public class FragmentEvents extends ObserverFragment implements FragmentEventsView {

    @Inject
    FragmentEventsPresenter fragmentEventsPresenter;

    @Bind(R.id.eventsRecyclerView)
    RecyclerView eventsRecyclerView;
    @Bind(R.id.empty_view)
    TextView emptyAdapterTextView;

    private SwipeRefreshLayout swipeRefreshLayout;
    private EventsRecyclerViewAdapter eventsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        if (swipeRefreshLayout == null) {
            swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_events_list, container, false);

            ButterKnife.bind(this, swipeRefreshLayout);

            swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
            swipeRefreshLayout.setOnRefreshListener(fragmentEventsPresenter::fetchEventsFromServer);
        }

//        preferences = getActivity().getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);

        return swipeRefreshLayout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventsRecyclerView.setHasFixedSize(true);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        if (eventsAdapter == null) {
            eventsAdapter = new EventsRecyclerViewAdapter(EventiApplication.applicationContext);
        }
        eventsRecyclerView.setAdapter(eventsAdapter);

        fragmentEventsPresenter.setView(this);
        fragmentEventsPresenter.fetchLastUpdatedTimestamp();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (eventsAdapter != null) {
            if (eventsAdapter.getItemCount() == 0) {
                emptyAdapterTextView.setVisibility(View.VISIBLE);
            } else {
                emptyAdapterTextView.setVisibility(View.GONE);
            }
        }

        // TODO check this on telephone
//        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
//            swipeRefreshLayout.setRefreshing(false);
//        }

//        String lastUpdateDate = preferences.getString(SHARED_PREFERENCE_LAST_UPDATE_OF_EVENTS, "");
//        Date today = new Date();
//        String todayDate = DateFormatterUtils.compareDateFormat.format(today);
//        if (!todayDate.equals(lastUpdateDate)) {
//            // get new events from server
//            fragmentEventsPresenter.fetchEventsFromServer();
//            preferences.edit().putString(SHARED_PREFERENCE_LAST_UPDATE_OF_EVENTS, todayDate).apply();
//        } else {
//            // just load current events from database
//            fragmentEventsPresenter.fetchEvents();
//        }

        fragmentEventsPresenter.fetchEvents();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (swipeRefreshLayout != null) {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            swipeRefreshLayout.removeAllViews();
            swipeRefreshLayout = null;
        }
    }

    public static FragmentEvents newInstance() {
        return new FragmentEvents();
    }

    @Override
    public void filterList(final String filter) {
        fragmentEventsPresenter.filterEvents(filter);
    }

    @Override
    public void showEvents(ArrayList<Event> eventArrayList) {
        if (eventsAdapter != null) {
            eventsAdapter.addEvents(eventArrayList);

            eventsAdapter.notifyDataSetChanged();

            if (eventsAdapter.getItemCount() > 0) {
                if (emptyAdapterTextView != null) {
                    emptyAdapterTextView.setVisibility(View.GONE);
                }
            }

            if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    @Override
    public void showNoEventsView() {
        emptyAdapterTextView.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideNoEventsView() {
        if (emptyAdapterTextView.getVisibility() == View.VISIBLE) {
            getActivity().runOnUiThread(() -> emptyAdapterTextView.setVisibility(View.GONE));
        }
    }

    @Override
    public void showLastUpdatedTimestampMessage(String lastUpdateTimestamp) {
        Snackbar.make(eventsRecyclerView,
                "Last updated: " + lastUpdateTimestamp,
                Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showNoInternetConnectionMessage() {
        Snackbar.make(eventsRecyclerView,
                R.string.no_internet_connection_cached_events,
                Snackbar.LENGTH_LONG)
                .show();
    }
}