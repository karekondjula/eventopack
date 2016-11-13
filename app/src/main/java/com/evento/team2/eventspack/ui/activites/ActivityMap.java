package com.evento.team2.eventspack.ui.activites;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.ui.fragments.FragmentMap;
import com.evento.team2.eventspack.utils.EventiConstants;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.IoniconsModule;

import butterknife.ButterKnife;

/**
 * Created by Daniel on 01-Oct-15.
 */
public class ActivityMap extends BaseAppCompatActivity {

    static {
        Iconify.with(new IoniconsModule());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentMap fragmentMap;

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

    @Override
    protected void onResume() {
        super.onResume();
        checkIfLocationIsOn();
    }

    private void checkIfLocationIsOn() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        try {
            if (locationManager != null) {
                gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }
        } catch(Exception ex) {}

        if(!gpsEnabled && !networkEnabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getResources().getString(R.string.gps_network_not_enabled_title));
            dialog.setMessage(getResources().getString(R.string.gps_network_not_enabled_message));
            dialog.setPositiveButton(getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.setNegativeButton(this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                }
            });
            dialog.show();
        }
    }
    
    public static Intent createIntent(Context context, int what, long id) {
        Intent intent = new Intent(context, ActivityMap.class);
        intent.putExtra(FragmentMap.EXTRA_WHAT, what);
        intent.putExtra(FragmentMap.EXTRA_ID, id);

        return intent;
    }
}