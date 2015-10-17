package com.evento.team2.eventspack.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.adapter.SmallLayoutEventsRecyclerViewAdapter;
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.evento.team2.eventspack.ui.activites.ActivityEventDetails;
import com.evento.team2.eventspack.utils.Utils;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 16-Aug-15.
 */
public class FragmentCalendar extends Fragment {
    public static final String TAG = "FragmentCalendar";

    private final static long DAY_IN_SECONDS = 24 * 60 * 60 * 1000;

    private CaldroidFragment caldroidFragment;
    private HashSet<Date> selectedDates;
    private SmallLayoutEventsRecyclerViewAdapter eventsAdapter;
    private ArrayList<Event> eventsList;
    private HashSet<Date> eventDates;

    @Bind(R.id.caldroidCalendar)
    FrameLayout calendarContainer;

    @Bind(R.id.calendarEventsLinearLayout)
    LinearLayout calendarEventsLinearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this, view);

        selectedDates = new HashSet<Date>();
        eventDates = new HashSet<Date>();

        caldroidFragment = new CaldroidFragment();
        caldroidFragment.setCaldroidListener(listener);
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        FragmentTransaction t = getChildFragmentManager().beginTransaction();
        t.replace(R.id.caldroidCalendar, caldroidFragment);
        t.commit();

        // TODO do not forget me please :*
//        if (NetworkUtils.getInstance().isNetworkAvailable(context)) {
        if (true) {
            // fetch all the events
            eventsList = Utils.Helpers.createEvents();
        } else {
            // fetch only saved events from database
            eventsList = EventsDatabase.getInstance().getAllSavedEvents();
        }

        Date eventDate;
        for (Event event : eventsList) {
            eventDate = new Date(event.date);
            caldroidFragment.setBackgroundResourceForDate(R.color.colorPrimaryDark, eventDate);
            cal.setTimeInMillis(eventDate.getTime());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            eventDates.add(cal.getTime());
        }

        caldroidFragment.setBackgroundResourceForDate(R.color.colorPrimary, new Date(System.currentTimeMillis()));


        eventsAdapter = new SmallLayoutEventsRecyclerViewAdapter(getActivity(), eventsList);
//        calendarRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        calendarRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        calendarRecyclerView.setAdapter(eventsAdapter);

//        int viewHeight = eventsAdapter.getItemCount() * adapterData.size();
//        calendarRecyclerView.getLayoutParams().height = viewHeight;

        return view;
    }

    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    final CaldroidListener listener = new CaldroidListener() {

        @Override
        public void onSelectDate(Date date, View view) {
            // TODO daniel add color animation?!
            if (Math.abs(date.getTime() - new Date().getTime()) > DAY_IN_SECONDS) {
                if (selectedDates.contains(date)) {
                    if (eventDates.contains(date)) {
                        // color it back with the event day color
                        caldroidFragment.setBackgroundResourceForDate(R.color.colorPrimaryDark, date);
                    } else {
                        // this is ordinary day
                        caldroidFragment.clearBackgroundResourceForDate(date);
                    }
                    selectedDates.remove(date);
                } else {
                    caldroidFragment.setBackgroundResourceForDate(R.color.colorAccent, date);
                    selectedDates.add(date);
                }
                caldroidFragment.refreshView();
            }

            fetchEventsOnSelectedDates(selectedDates);
        }

        @Override
        public void onChangeMonth(int month, int year) {
        }

        @Override
        public void onLongClickDate(Date date, View view) {
        }

        @Override
        public void onCaldroidViewCreated() {
        }
    };

    private void fetchEventsOnSelectedDates(HashSet<Date> selectedDates) {
        // TODO daniel fetch all events for the current date
        for (final Event event : eventsList) {

            final View calendarItemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_small_events, calendarEventsLinearLayout, false);
            ImageView calendarEventImageView = (ImageView) ButterKnife.findById(calendarItemView, R.id.small_event_picture);
            calendarEventImageView.setImageResource(R.drawable.party_image);
//                Glide.with(getActivity()).load(R.drawable.party_image).into(calendarEventImageView);
            ((TextView) ButterKnife.findById(calendarItemView, R.id.event_title)).setText(event.name);
            ((TextView) ButterKnife.findById(calendarItemView, R.id.event_details)).setText(event.details);
            calendarItemView.setClickable(true);
            calendarItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ActivityEventDetails.class);
                    intent.putExtra(ActivityEventDetails.EXTRA_NAME, event.name);
                    if (!TextUtils.isEmpty("")) {
                        intent.putExtra(ActivityEventDetails.EXTRA_PICTURE_URI, "");
                    }
                    getActivity().startActivity(intent);
                }
            });
            calendarEventsLinearLayout.addView(calendarItemView);
        }
    }

    public static FragmentCalendar newInstance() {
        FragmentCalendar fragmentCalendar = new FragmentCalendar();
        return fragmentCalendar;
    }
}
