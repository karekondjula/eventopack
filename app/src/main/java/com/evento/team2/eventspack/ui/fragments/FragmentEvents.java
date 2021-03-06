package com.evento.team2.eventspack.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.adapters.EventsAdapter;
import com.evento.team2.eventspack.adapters.viewholders.EventViewHolder;
import com.evento.team2.eventspack.components.AppComponent;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.presenters.interfaces.FragmentEventsPresenter;
import com.evento.team2.eventspack.ui.activites.ActivityEventDetails;
import com.evento.team2.eventspack.ui.components.EndlessRecyclerViewScrollListener;
import com.evento.team2.eventspack.ui.components.EventsItemTouchHelperCallback;
import com.evento.team2.eventspack.ui.fragments.interfaces.BaseFragment;
import com.evento.team2.eventspack.utils.EventiConstants;
import com.evento.team2.eventspack.views.FragmentEventsView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.evento.team2.eventspack.adapters.viewholders.EventViewHolder.ICON_TEXT_VIEW_EMPTY_HEART;
import static com.evento.team2.eventspack.adapters.viewholders.EventViewHolder.ICON_TEXT_VIEW_EMPTY_HEART_SPIN;
import static com.evento.team2.eventspack.adapters.viewholders.EventViewHolder.ICON_TEXT_VIEW_FILLED_HEART;
import static com.evento.team2.eventspack.adapters.viewholders.EventViewHolder.ICON_TEXT_VIEW_FILLED_HEART_SPIN;

/**
 * Created by Daniel on 31-Jul-15.
 */
public class FragmentEvents extends BaseFragment implements FragmentEventsView, EventViewHolder.EventListener {

    public static final String TAG = "FragmentEvents";
    public static final int NO_CHANGE_IN_PAGE_SIZE_VALUE = 0;

    @Inject
    FragmentEventsPresenter fragmentEventsPresenter;

    @BindView(R.id.eventsRecyclerView)
    RecyclerView eventsRecyclerView;

    @BindView(R.id.empty_view)
    TextView emptyAdapterTextView;

    SwipeRefreshLayout swipeRefreshLayout;
    EventsAdapter eventsAdapter;

    LinearLayoutManager layoutManager;

    private EventViewHolder lastClickedEventVH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_events_list, container, false);

        unbinder = ButterKnife.bind(this, swipeRefreshLayout);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(() -> fragmentEventsPresenter.fetchEventsFromServer(true));

        layoutManager = new LinearLayoutManager(getContext());
//        layoutManager = new GridLayoutManager(getContext(), 2);
        eventsRecyclerView.setLayoutManager(layoutManager);
        eventsRecyclerView.setHasFixedSize(true);
        eventsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                fragmentEventsPresenter.fetchEvents(lastQuery, (page + 1) * EventiConstants.OFFSET);
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

        return swipeRefreshLayout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventsAdapter = new EventsAdapter(getActivity(), this);
        eventsRecyclerView.setAdapter(eventsAdapter);
        ItemTouchHelper.Callback callback = new EventsItemTouchHelperCallback(eventsAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(eventsRecyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();

        fragmentEventsPresenter.setView(this);
        fragmentEventsPresenter.fetchLastUpdatedTimestamp();
        fragmentEventsPresenter.fetchEventsFromServer(false);
//        fragmentEventsPresenter.fetchEvents(lastQuery, NO_CHANGE_IN_PAGE_SIZE_VALUE);

        if (lastClickedEventVH != null) {
            if (lastClickedEventVH.getEvent().isEventSaved) {
                lastClickedEventVH.getIsEventSaved().setText(ICON_TEXT_VIEW_FILLED_HEART);
            } else {
                lastClickedEventVH.getIsEventSaved().setText(ICON_TEXT_VIEW_EMPTY_HEART);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
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
        fragmentEventsPresenter.fetchEvents(filter, 0);
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @Override
    public void showEvents(final ArrayList<Event> eventsArrayList) {
        if (eventsAdapter != null) {

            if (eventsArrayList == null) { // empty database
                if (emptyAdapterTextView != null) {
                    emptyAdapterTextView.setVisibility(View.VISIBLE);
                }
            } else {
                if (emptyAdapterTextView != null) {
                    emptyAdapterTextView.setVisibility(View.GONE);
                }

                eventsAdapter.updateView(eventsArrayList);
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
        Snackbar.make(coordinatorLayout,
                String.format(getString(R.string.last_updated), lastUpdateTimestamp),
                Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showNoInternetConnectionMessage() {
        Snackbar.make(coordinatorLayout,
                R.string.no_internet_connection_cached_events,
                Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void notifyUserForSavedEvent(String eventName) {
        Snackbar.make(coordinatorLayout,
                getString(R.string.event_is_saved),
                Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onHeartClicked(EventViewHolder eventViewHolder) {

        if (!eventViewHolder.getEvent().isEventSaved) {
            eventViewHolder.getIsEventSaved().setText(ICON_TEXT_VIEW_FILLED_HEART);
        } else {
            eventViewHolder.getIsEventSaved().setText(ICON_TEXT_VIEW_EMPTY_HEART);
        }

        fragmentEventsPresenter.changeSavedStateOfEvent(eventViewHolder.getEvent());
    }

    @Override
    public void onEventClicked(EventViewHolder eventViewHolder) {
        lastClickedEventVH = eventViewHolder;

        if (eventViewHolder.getEvent().isEventSaved) {
            eventViewHolder.getIsEventSaved().setText(ICON_TEXT_VIEW_FILLED_HEART_SPIN);
        } else {
            eventViewHolder.getIsEventSaved().setText(ICON_TEXT_VIEW_EMPTY_HEART_SPIN);
        }

        Activity activity = getActivity();
        Intent intent = ActivityEventDetails.createIntent(getActivity(), eventViewHolder.getEvent().id);

//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                ActivityOptions options = ActivityOptions
//                        .makeSceneTransitionAnimation(activity, holder.mEventTitle, EventiConstants.TRANSITION_EVENT_IMAGE);
//
//                activity.startActivity(intent, options.toBundle());
//            } else {
        activity.startActivity(intent);
//            }
    }

    @Override
    public void onEventSwiped(EventViewHolder eventViewHolder) {
        fragmentEventsPresenter.deleteEvent(eventViewHolder.getEvent());

        Snackbar.make(coordinatorLayout, getString(R.string.event_is_removed), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.undo), view -> {
                    Event event = eventViewHolder.getEvent();
                    fragmentEventsPresenter.undoDelete(event);
                }).show();
    }
}