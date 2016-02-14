package com.evento.team2.eventspack.modules;

import android.view.View;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.presenters.interfaces.FragmentCalendarPresenter;
import com.evento.team2.eventspack.utils.ColorUtils;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Date;

import javax.inject.Singleton;

import dagger.Provides;

/**
 * Created by Daniel on 13-Feb-16.
 */
public class CalendarModule {

    @Provides
    @Singleton
    CaldroidFragment provideCaldroidFragment() {
        return new CaldroidFragment();
    }

    @Provides
    @Singleton
    CaldroidListener provideCaldroidListener(FragmentCalendarPresenter fragmentCalendarPresenter) {
        CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
//                long dateLong = date.getTime();
//
//                if (selectedDates.contains(dateLong)) { // remove from selected
//                    if (eventDates.contains(dateLong)) { // a day with events returns to dark green
//                        caldroidFragment.setBackgroundResourceForDate(R.color.colorPrimaryDark, date);
//                    } else { // day with no events return to white background
//                        caldroidFragment.clearBackgroundResourceForDate(date);
//                    }
//                    selectedDates.remove(dateLong);
//                } else { // add to selected
//                    int color;
//                    if (dateColorHashMap.containsKey(dateLong)) {
//                        color = dateColorHashMap.get(dateLong);
//                    } else {
//                        color = ColorUtils.getInstance().getRandomColor(EventiApplication.applicationContext);
//                        dateColorHashMap.put(dateLong, color);
//                    }
//
//                    caldroidFragment.setBackgroundResourceForDate(color, date);
//                    selectedDates.add(dateLong);
//                }
//
//                calendarEventsLinearLayout.removeAllViews();
//
//                new Thread() {
//                    @Override
//                    public void run() {
//                        fetchEventsOnSelectedDates(selectedDates);
//                    }
//                }.start();
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

        return listener;
    }
}
