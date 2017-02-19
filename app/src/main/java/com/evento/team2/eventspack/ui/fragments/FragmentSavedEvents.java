package com.evento.team2.eventspack.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.adapters.EventsAdapter;
import com.evento.team2.eventspack.adapters.viewholders.EventViewHolder;
import com.evento.team2.eventspack.components.AppComponent;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.presenters.FragmentSavedEventsPresenterImpl;
import com.evento.team2.eventspack.ui.activites.ActivityEventDetails;
import com.evento.team2.eventspack.ui.fragments.interfaces.BaseFragment;
import com.evento.team2.eventspack.utils.EventiConstants;
import com.evento.team2.eventspack.views.FragmentEventsView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.evento.team2.eventspack.adapters.viewholders.EventViewHolder.ICON_TEXT_VIEW_EMPTY_HEART;
import static com.evento.team2.eventspack.adapters.viewholders.EventViewHolder.ICON_TEXT_VIEW_FILLED_HEART;

/**
 * Created by Daniel on 31-Jul-15.
 */
public class FragmentSavedEvents extends BaseFragment implements FragmentEventsView, EventViewHolder.EventListener {

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

        savedEventsAdapter = new EventsAdapter(getActivity(), this);
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
            savedEventsAdapter.updateView(eventsArrayList);
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
    public void notifyUserForSavedEvent(String eventName) {
        // no op
    }

//    @Override
//    public void notifyUserForSavedEvent(boolean isSaved, String eventName) {
//        Snackbar.make(savedEventsRecyclerView,
//                isSaved ?
//                        String.format(getResources().getString(R.string.event_is_saved), eventName) :
//                        String.format(getResources().getString(R.string.event_is_removed), eventName),
//                Snackbar.LENGTH_LONG)
//                .show();
//    }

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

    }
}