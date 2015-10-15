package com.evento.team2.eventspack.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.evento.team2.eventspack.utils.NetworkUtils;
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

    private final static long DAY_IN_SECONDS = 24 * 60 * 60 * 1000;
    public static final String TAG = "FragmentCalendar";

    CaldroidFragment caldroidFragment;

    private HashSet<Date> selectedDates;
    private HashSet<Date> eventDates;

    @Bind(R.id.caldroidCalendar)
    FrameLayout calendarContainer;

//    @Bind(R.id.calendarEventsLayout)
//    LinearLayout calendarEventsLayout;

    private Context context;

    private ArrayList<Event> eventsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this, view);

        context = getContext();

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

        calendarContainer.getViewTreeObserver().addOnGlobalLayoutListener(CalendarViewTreeObserver);

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

            // TODO daniel fetch all events for the current date
//            for (final Event event : eventsList) {
//
//                final CardView calendarItemView = (CardView) LayoutInflater.from(context).inflate(R.layout.item_calendar_events, calendarEventsLayout, false);
//                ImageView calendar_event_picture = (ImageView) ButterKnife.findById(calendarItemView, R.id.calendar_event_picture);
//                calendar_event_picture.setImageResource(R.drawable.party_image);
////                Glide.with(context).load(R.drawable.party_image).into(calendar_event_picture);
//                ((TextView) ButterKnife.findById(calendarItemView, R.id.name)).setText(event.name);
//                ((TextView) ButterKnife.findById(calendarItemView, R.id.details)).setText(event.details);
//                calendarItemView.setClickable(true);
//                calendarItemView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent motionEvent) {
////                        if (motionEvent.getDownTime() < 500) {
//                            Intent intent = new Intent(context, ActivityEventDetails.class);
//                            intent.putExtra(ActivityEventDetails.EXTRA_NAME, event.name);
//                            if (!TextUtils.isEmpty("")) {
//                                intent.putExtra(ActivityEventDetails.EXTRA_PICTURE_URI, "");
//                            }
//                            context.startActivity(intent);
//
//                        return false;
//                    }
//                });
////                calendarItemView.setOnClickListener(new OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        Intent intent = new Intent(context, ActivityEventDetails.class);
////                        intent.putExtra(ActivityEventDetails.EXTRA_NAME, event.name);
////                        if (!TextUtils.isEmpty("")) {
////                            intent.putExtra(ActivityEventDetails.EXTRA_PICTURE_URI, "");
////                        }
////                        context.startActivity(intent);
////                    }
////                });
//                calendarEventsLayout.addView(calendarItemView);
//
//            }
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
        FragmentCalendar fragmentCalendar = new FragmentCalendar();
        return fragmentCalendar;
    }

    private ViewTreeObserver.OnGlobalLayoutListener CalendarViewTreeObserver = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            calendarContainer.getViewTreeObserver().removeOnGlobalLayoutListener(CalendarViewTreeObserver);
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) calendarContainer.getLayoutParams();
            lp.height = ((ViewGroup)calendarContainer).getChildAt(0).getHeight();
            calendarContainer.setLayoutParams(lp);
            calendarContainer.requestLayout();
        }
    };
}
