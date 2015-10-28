package com.evento.team2.eventspack.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.evento.team2.eventspack.ui.activites.ActivityEventDetails;
import com.evento.team2.eventspack.utils.ColorUtils;
import com.evento.team2.eventspack.utils.Utils;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Daniel on 16-Aug-15.
 */
public class FragmentCalendar extends Fragment {
    public static final String TAG = "FragmentCalendar";

    private final static Date TODAY = new Date();

    private CaldroidFragment caldroidFragment;
    private ArrayList<Long> selectedDates;
    private ArrayList<Event> eventsList;
    private ArrayList<Long> eventDates;
    private HashMap<Long, Integer> dateColorHashMap;

    @Bind(R.id.caldroidCalendar)
    FrameLayout calendarContainer;

    @Bind(R.id.calendarEventsLinearLayout)
    LinearLayout calendarEventsLinearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this, view);

        selectedDates = new ArrayList<>();
        eventDates = new ArrayList<>();
        dateColorHashMap = new HashMap();

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

//        eventsList = EventsDatabase.getInstance().getSavedEvents(null);
        eventsList = Utils.Helpers.createEvents();
//        Collections.sort(eventsList); // TODO events list must be sorted by date

        // TODO daniel take this of main thread
        Date eventDate;
        for (Event event : eventsList) {
            eventDate = new Date(event.startDate);
            caldroidFragment.setBackgroundResourceForDate(R.color.colorPrimaryDark, eventDate);
            cal.setTimeInMillis(eventDate.getTime());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            eventDates.add(cal.getTime().getTime());
        }

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
            long dateLong = date.getTime();

            if (selectedDates.contains(dateLong)) {
                if (eventDates.contains(dateLong)) { // a day with events
                    caldroidFragment.setBackgroundResourceForDate(R.color.colorPrimaryDark, date);
                } else {
                    if (date.getTime() == TODAY.getTime()) {
                        caldroidFragment.setBackgroundResourceForDate(R.color.colorPrimary, date);
                    } else { // not today and no events
                        caldroidFragment.clearBackgroundResourceForDate(date);
                    }
                }
                selectedDates.remove(dateLong);
            } else {
                int color;
                if (dateColorHashMap.containsKey(dateLong)) {
                    color = dateColorHashMap.get(dateLong);
                } else {
                    color = ColorUtils.getInstance().getRandomColor(getActivity());
                    dateColorHashMap.put(dateLong, color);
                }

                caldroidFragment.setBackgroundResourceForDate(color, date);
                selectedDates.add(dateLong);
            }
            fetchEventsOnSelectedDates(selectedDates);

            caldroidFragment.refreshView();
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

    private void fetchEventsOnSelectedDates(ArrayList<Long> selectedDates) {
        calendarEventsLinearLayout.removeAllViews();
        // fetch all events for the current startDate
        for (final Event event : eventsList) {
            if (selectedDates.contains(event.startDate)) {
                final View calendarItemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_small_events, calendarEventsLinearLayout, false);
                ImageView calendarEventImageView = (ImageView) ButterKnife.findById(calendarItemView, R.id.small_event_picture);
                ((CircleImageView) ButterKnife.findById(calendarItemView, R.id.event_color)).setImageResource(dateColorHashMap.get(event.startDate));
                calendarEventImageView.setImageResource(R.drawable.party_image);
//                Glide.with(getActivity()).load(R.drawable.party_image).into(calendarEventImageView);
                ((TextView) ButterKnife.findById(calendarItemView, R.id.event_title)).setText(event.name);
                ((TextView) ButterKnife.findById(calendarItemView, R.id.event_details)).setText(event.details);
                calendarItemView.setClickable(true);
                calendarItemView.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), ActivityEventDetails.class);
                    intent.putExtra(ActivityEventDetails.EXTRA_NAME, event.name);
                    if (!TextUtils.isEmpty("")) {
                        intent.putExtra(ActivityEventDetails.EXTRA_PICTURE_URI, "");
                    }
                    getActivity().startActivity(intent);
                });
                calendarEventsLinearLayout.addView(calendarItemView);
            }
        }
    }

    public static FragmentCalendar newInstance() {
        FragmentCalendar fragmentCalendar = new FragmentCalendar();
        return fragmentCalendar;
    }
}
