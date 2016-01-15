package com.evento.team2.eventspack.ui.activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.provider.FetchAsyncTask;
import com.evento.team2.eventspack.ui.fragments.FragmentMap;
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

//        final ActionBar actionbar = getSupportActionBar();
//        if (actionbar != null) {
//            actionbar.setDisplayHomeAsUpEnabled(true);
//            actionbar.setTitle("Calendar");
//        }

        FragmentMap fragmentMap = null;

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                int what = bundle.getInt(FragmentMap.EXTRA_WHAT);
                long id = bundle.getLong(FragmentMap.EXTRA_ID);
                switch (what) {
                    case FetchAsyncTask.SAVED_EVENTS:
                        // TODO go to saved events in activity map
                    case FetchAsyncTask.EVENTS:
                        fragmentMap = FragmentMap.newInstance(FetchAsyncTask.EVENTS, id);
                        break;
                    case FetchAsyncTask.PLACES:
                        fragmentMap = FragmentMap.newInstance(FetchAsyncTask.PLACES, id);
                        break;

                    default:
                        fragmentMap = FragmentMap.newInstance(FetchAsyncTask.NONE, FetchAsyncTask.NO_EVENT_ID);
                }
            } else {
                fragmentMap = FragmentMap.newInstance(FetchAsyncTask.NONE, FetchAsyncTask.NO_EVENT_ID);
            }
        }

        getFragmentManager().beginTransaction()
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