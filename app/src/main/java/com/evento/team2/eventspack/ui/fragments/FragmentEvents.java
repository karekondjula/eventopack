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

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.adapters.EventsAdapter;
import com.evento.team2.eventspack.components.AppComponent;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.presenters.interfaces.FragmentEventsPresenter;
import com.evento.team2.eventspack.ui.fragments.interfaces.BaseFragment;
import com.evento.team2.eventspack.ui.components.EndlessRecyclerViewScrollListener;
import com.evento.team2.eventspack.utils.EventiConstants;
import com.evento.team2.eventspack.views.FragmentEventsView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 31-Jul-15.
 */
public class FragmentEvents extends BaseFragment implements FragmentEventsView {

    private static final int OFFSET = 8;

    @Inject
    FragmentEventsPresenter fragmentEventsPresenter;

    @BindView(R.id.eventsRecyclerView)
    RecyclerView eventsRecyclerView;

    @BindView(R.id.empty_view)
    TextView emptyAdapterTextView;

    SwipeRefreshLayout swipeRefreshLayout;
    private EventsAdapter eventsAdapter;

    String lastQuery = EventiConstants.NO_FILTER_STRING;
    LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_events_list, container, false);

        unbinder = ButterKnife.bind(this, swipeRefreshLayout);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(() -> fragmentEventsPresenter.fetchEventsFromServer(true));

        linearLayoutManager = new LinearLayoutManager(getContext());
        eventsRecyclerView.setLayoutManager(linearLayoutManager);
        eventsRecyclerView.setHasFixedSize(true);
        eventsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
//                loadNextDataFromApi(page);
                fragmentEventsPresenter.fetchEvents(lastQuery, (page + 1) * OFFSET);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int topRowVerticalPosition =
                        (eventsRecyclerView == null || eventsRecyclerView.getChildCount() == 0) ?
                                0 : eventsRecyclerView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        };
        eventsRecyclerView.addOnScrollListener(scrollListener);
//        eventsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                int topRowVerticalPosition =
//                        (eventsRecyclerView == null || eventsRecyclerView.getChildCount() == 0) ?
//                                0 : eventsRecyclerView.getChildAt(0).getTop();
//                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
//
//                Log.d(">> last vise", linearLayoutManager.findLastVisibleItemPosition() + "");
//                Log.d(">> child count", linearLayoutManager.getItemCount() + "");
//                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == linearLayoutManager.getItemCount() - 2){
//                    fragmentEventsPresenter.fetchEvents(lastQuery, OFFSET);
//                }
//            }
//        });

        return swipeRefreshLayout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        eventsRecyclerView.setHasFixedSize(true);
//        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        eventsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        eventsAdapter = new EventsAdapter(getActivity(), fragmentEventsPresenter);
        eventsRecyclerView.setAdapter(eventsAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        fragmentEventsPresenter.setView(this);
        fragmentEventsPresenter.fetchLastUpdatedTimestamp();
        fragmentEventsPresenter.fetchEventsFromServer(false);
        fragmentEventsPresenter.fetchEvents(lastQuery, OFFSET);
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
        lastQuery = filter;
        fragmentEventsPresenter.fetchEvents(lastQuery, OFFSET);
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @Override
    public void showEvents(ArrayList<Event> eventsArrayList) {
        if (eventsAdapter != null) {

            if (eventsArrayList == null) {
                // empty database
                if (emptyAdapterTextView != null) {
                    emptyAdapterTextView.setVisibility(View.VISIBLE);
                }
            } else {
                if (emptyAdapterTextView != null) {
                    emptyAdapterTextView.setVisibility(View.GONE);
                }

                // some kind of optimization ... further read is required
//            eventsAdapter = new EventsAdapter(getActivity());
//            eventsRecyclerView.swapAdapter(eventsAdapter, false);
                eventsAdapter.addEvents(eventsArrayList);
//                eventsAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void startRefreshAnimation() {
        if (swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void stopRefreshAnimation() {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
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

    @Override
    public void notifyUserForUpdateInEvent(boolean isSaved, String eventName) {

        Snackbar.make(eventsRecyclerView,
                isSaved ?
                        String.format(getResources().getString(R.string.event_is_saved), eventName) :
                        String.format(getResources().getString(R.string.event_is_removed), eventName),
                Snackbar.LENGTH_LONG)
                .show();
    }
}