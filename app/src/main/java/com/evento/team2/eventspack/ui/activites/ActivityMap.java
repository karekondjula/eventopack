package com.evento.team2.eventspack.ui.activites;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.evento.team2.eventspack.provider.FetchAsyncTask;
import com.evento.team2.eventspack.ui.fragments.FragmentCalendar;
import com.evento.team2.eventspack.ui.fragments.FragmentMap;
import com.evento.team2.eventspack.utils.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 01-Oct-15.
 */
public class ActivityMap extends AppCompatActivity {

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

        FragmentMap fragmentMap;

        Intent intent = getIntent();
        if (intent != null) {
            int whatInt = intent.getIntExtra(FragmentMap.EXTRA_WHAT, -1) ;
            FetchAsyncTask.Category what;
            switch (whatInt){
                case 0: fragmentMap = FragmentMap.newInstance(FetchAsyncTask.EVENTS,
                        intent.getIntExtra(FragmentMap.EXTRA_ID, -1));
                    break;
                case 1: fragmentMap = FragmentMap.newInstance(FetchAsyncTask.PLACES,
                        intent.getIntExtra(FragmentMap.EXTRA_ID, -1));
                    break;
                default:
                    fragmentMap = FragmentMap.newInstance(FetchAsyncTask.NONE, 0);
            }
        } else {
            fragmentMap = FragmentMap.newInstance(FetchAsyncTask.NONE, 0);
        }

        getFragmentManager().beginTransaction()
                .add(R.id.fragment_map_frame, fragmentMap, FragmentMap.TAG)
                .commit();
    }
}