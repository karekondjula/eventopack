package com.evento.team2.eventspack.ui.activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.ui.fragments.FragmentCalendar;

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
            actionbar.setTitle(R.string.calendar);
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_calendar_frame, FragmentCalendar.newInstance(), FragmentCalendar.TAG)
                .commit();
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, ActivityCalendar.class);
        return intent;
    }
}
