package com.evento.team2.eventspack.ui.activites;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.ui.fragments.FragmentCalendar;
import com.roomorama.caldroid.CaldroidFragment;

import java.util.Calendar;

import butterknife.ButterKnife;

public class ActivityCalendar extends AppCompatActivity {
    private static final String TAG = "ActivityCalendar";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Calendar");
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_calendar_frame, FragmentCalendar.newInstance(), FragmentCalendar.TAG)
                .commit();

//        CaldroidFragment caldroidFragment = new CaldroidFragment();
//        Bundle args = new Bundle();
//        Calendar cal = Calendar.getInstance();
//        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
//        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
//        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
//        caldroidFragment.setArguments(args);
//
//        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
//        t.replace(R.id.fragment_calendar_frame, caldroidFragment);
//        t.commit();
    }
}
