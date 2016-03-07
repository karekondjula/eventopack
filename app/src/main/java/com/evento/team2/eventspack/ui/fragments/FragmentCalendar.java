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

import com.bumptech.glide.Glide;
import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.components.CalendarComponent;
import com.evento.team2.eventspack.components.DaggerCalendarComponent;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.modules.CalendarModule;
import com.evento.team2.eventspack.presenters.interfaces.FragmentCalendarPresenter;
import com.evento.team2.eventspack.ui.activites.ActivityEventDetails;
import com.evento.team2.eventspack.utils.ColorUtils;
import com.evento.team2.eventspack.views.FragmentCalendarView;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Daniel on 16-Aug-15.
 */
public class FragmentCalendar extends Fragment implements FragmentCalendarView {
    public static final String TAG = "FragmentCalendar";

    private final static Date TODAY = new Date();

    private ArrayList<Long> selectedDates;
    private ArrayList<Long> eventDates;

    @Bind(R.id.caldroidCalendar)
    FrameLayout calendarContainer;

    @Bind(R.id.calendarEventsLinearLayout)
    LinearLayout calendarEventsLinearLayout;

    @Inject
    CaldroidFragment caldroidFragment;

    @Inject
    FragmentCalendarPresenter fragmentCalendarPresenter;

    @Inject
    ColorUtils colorUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this, view);

        selectedDates = new ArrayList<>();
        eventDates = new ArrayList<>();

        caldroidFragment.setCaldroidListener(listener);
        Bundle args = new Bundle();
        Calendar calendar = Calendar.getInstance();
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        args.putInt(CaldroidFragment.MONTH, calendar.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, calendar.get(Calendar.YEAR));
//        args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false); // maybe use it in smaller devices
        caldroidFragment.setArguments(args);

        FragmentTransaction t = getChildFragmentManager().beginTransaction();
        t.replace(R.id.caldroidCalendar, caldroidFragment);
        t.commit();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        fragmentCalendarPresenter.setView(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalendarComponent calendarComponent = DaggerCalendarComponent.builder()
                .appComponent(((EventiApplication) getActivity().getApplication()).getAppComponent())
                .calendarModule(new CalendarModule())
                .build();

        calendarComponent.inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentCalendarPresenter.fetchEvents();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    final CaldroidListener listener = new CaldroidListener() {

        @Override
        public void onSelectDate(Date date, View view) {
            long dateLong = date.getTime();

            if (selectedDates.contains(dateLong)) { // remove from selected
                if (eventDates.contains(dateLong)) { // a day with events returns to dark green
                    caldroidFragment.setBackgroundResourceForDate(R.color.colorPrimaryDark, date);
                } else { // day with no events return to white background
                    caldroidFragment.clearBackgroundResourceForDate(date);
                }
                selectedDates.remove(dateLong);
            } else { // add to selected
                int color;
                if (colorUtils.dateColorHashMap.containsKey(dateLong)) {
                    color = colorUtils.dateColorHashMap.get(dateLong);
                } else {
                    color = colorUtils.getRandomColor();
                    colorUtils.dateColorHashMap.put(dateLong, color);
                }

                caldroidFragment.setBackgroundResourceForDate(color, date);
                selectedDates.add(dateLong);
            }

            caldroidFragment.refreshView();

            fragmentCalendarPresenter.fetchEventsOnSelectedDates(selectedDates);
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

    public static FragmentCalendar newInstance() {
        return new FragmentCalendar();
    }

    @Override
    public void showCalendarEvents(ArrayList<Event> eventArrayList) {
        if (getActivity() != null) {
            Date eventDate;
            Calendar calendar = Calendar.getInstance();

            caldroidFragment.setTextColorForDate(R.color.ColorCrimson, TODAY);

            for (Event event : eventArrayList) {
                eventDate = new Date(event.startDate);

                calendar.setTimeInMillis(eventDate.getTime());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                if (!eventDates.contains(calendar.getTime().getTime())) {
                    caldroidFragment.setBackgroundResourceForDate(R.color.colorPrimaryDark, eventDate);
                    eventDates.add(calendar.getTime().getTime());
                }
            }

            caldroidFragment.refreshView();
        }
    }

    @Override
    public void showEventsForSelectedDates(ArrayList<Event> eventArrayList) {

        calendarEventsLinearLayout.removeAllViews();

        if (getActivity() != null) {
            for (final Event event : eventArrayList) {
                final View calendarItemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_small, calendarEventsLinearLayout, false);
                ImageView calendarEventImageView = ButterKnife.findById(calendarItemView, R.id.small_event_picture);
                ((CircleImageView) ButterKnife.findById(calendarItemView, R.id.event_color)).setImageResource(colorUtils.dateColorHashMap.get(event.startDate));
                ((TextView) ButterKnife.findById(calendarItemView, R.id.event_title)).setText(event.name);
                ((TextView) ButterKnife.findById(calendarItemView, R.id.event_details)).setText(event.details);
                ((TextView) ButterKnife.findById(calendarItemView, R.id.event_time)).setText(event.startTimeString);
                calendarItemView.setClickable(true);
                calendarItemView.setOnClickListener(v -> {
                    Intent intent = ActivityEventDetails.createIntent(getActivity(), event.id);
                    getActivity().startActivity(intent);

                });

                getActivity().runOnUiThread(() -> {
                    if (TextUtils.isEmpty(event.pictureUri)) {
                        Glide.with(getActivity()).load(R.drawable.party_image).into(calendarEventImageView);
                    } else {
                        Glide.with(getActivity()).load(event.pictureUri).into(calendarEventImageView);
                    }
                    calendarEventsLinearLayout.addView(calendarItemView);
                });
            }

            caldroidFragment.refreshView();
        }
    }
}
