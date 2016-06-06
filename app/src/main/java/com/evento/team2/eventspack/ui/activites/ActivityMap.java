package com.evento.team2.eventspack.ui.activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.ui.fragments.FragmentMap;
import com.evento.team2.eventspack.utils.EventiConstants;
import com.google.android.gms.maps.SupportMapFragment;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.IoniconsModule;

import butterknife.ButterKnife;

/**
 * Created by Daniel on 01-Oct-15.
 */
public class ActivityMap extends AppCompatActivity {

    static {
        Iconify.with(new IoniconsModule());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentMap fragmentMap = null;

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                int what = bundle.getInt(FragmentMap.EXTRA_WHAT);
                long id = bundle.getLong(FragmentMap.EXTRA_ID);
                switch (what) {
                    case EventiConstants.SAVED_EVENTS:
                        // TODO go to saved events in activity menuItemMap
                    case EventiConstants.EVENTS:
                        fragmentMap = FragmentMap.newInstance(EventiConstants.EVENTS, id);
                        break;
                    case EventiConstants.PLACES:
                        fragmentMap = FragmentMap.newInstance(EventiConstants.PLACES, id);
                        break;
                    default:
                        fragmentMap = FragmentMap.newInstance(EventiConstants.EVENTS, EventiConstants.NO_EVENT_ID);
                }
            } else {
                fragmentMap = FragmentMap.newInstance(EventiConstants.EVENTS, EventiConstants.NO_EVENT_ID);
            }
        } else {
            fragmentMap = FragmentMap.newInstance(EventiConstants.EVENTS, EventiConstants.NO_EVENT_ID);
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_map_frame, fragmentMap, FragmentMap.TAG)
                .commit();
    }

    public static Intent createIntent(Context context, int what, long id) {
        Intent intent = new Intent(context, ActivityMap.class);
        intent.putExtra(FragmentMap.EXTRA_WHAT, what);
        intent.putExtra(FragmentMap.EXTRA_ID, id);

        return intent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}