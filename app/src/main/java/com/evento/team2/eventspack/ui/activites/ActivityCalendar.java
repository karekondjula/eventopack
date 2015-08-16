package com.evento.team2.eventspack.ui.activites;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.evento.team2.eventspack.utils.Utils;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import butterknife.ButterKnife;

/**
 * Created by Daniel on 16-Aug-15.
 */
// TODO clear all selections in settings or something
public class ActivityCalendar extends FragmentActivity {

    private final static long DAY_IN_SECONDS = 24*60*60*1000;

    private CaldroidFragment caldroidFragment;
    private HashSet<Date> selectedDates;
    private HashSet<Date> eventDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        selectedDates = new HashSet<Date>();
        eventDates = new HashSet<Date>();

//        Toolbar actionbar = ButterKnife.findById(this, R.id.toolbar);
//        actionbar.setTitle("Calendar");
//        actionbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getFragmentManager().popBackStack();
//            }
//        });

        caldroidFragment = new CaldroidFragment();
        caldroidFragment.setCaldroidListener(listener);
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        ArrayList<Event> list = null;
        if (/*has internet*/true) {
            // fetch all the events
            list = Utils.Helpers.createEvents();
        } else {
            // fetch only saved events from database
            list = EventsDatabase.getInstance().getAllSavedEvents();
        }

        Date eventDate;
        for (Event event : list) {
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

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_frame, caldroidFragment);
        t.commit();

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitle("Calendar");
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
        }

        @Override
        public void onChangeMonth(int month, int year) {}

        @Override
        public void onLongClickDate(Date date, View view) {}

        @Override
        public void onCaldroidViewCreated() {}
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (getFragmentManager().getBackStackEntryCount() == 1) {
            onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }

        return true;
    }
}
