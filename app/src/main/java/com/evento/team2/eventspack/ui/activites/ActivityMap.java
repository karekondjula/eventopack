package com.evento.team2.eventspack.ui.activites;

import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.utils.Utils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.ButterKnife;

/**
 * Created by Daniel on 01-Oct-15.
 */
public class ActivityMap extends AppCompatActivity implements OnMapReadyCallback {

    // TODO daniel butterify the code
    private MapFragment supportMapFragment;
    private GoogleMap mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        final Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(null);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("Events map");
        }

        FragmentManager fm = getFragmentManager();
        supportMapFragment = (MapFragment) fm.findFragmentById(R.id.location_map);
        if (supportMapFragment == null) {
            supportMapFragment = MapFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.location_map, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapView = googleMap;
        mapView.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mapView.setMyLocationEnabled(true);
        mapView.getUiSettings().setCompassEnabled(true);
        mapView.getUiSettings().setRotateGesturesEnabled(true);
        mapView.getUiSettings().setZoomControlsEnabled(true);
        mapView.getUiSettings().setZoomGesturesEnabled(true);

        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.party_image);
        Bitmap bhalfsize = Bitmap.createScaledBitmap(b, b.getWidth() / 4, b.getHeight() / 4, false);
        for (Event event : Utils.Helpers.createEvents()) {
            mapView.addMarker(new MarkerOptions()
                    .position(new LatLng(event.location.latitude,
                            event.location.longitude))
                    .title(event.name)
                    .icon(BitmapDescriptorFactory.fromBitmap(bhalfsize)));
        }

        mapView.getMyLocation();
//        SmartLocation.with(getActivity()).location()
//                .oneFix()
//                .start(new OnLocationUpdatedListener() {
//
//                    @Override
//                    public void onLocationUpdated(Location location) {
//                        mapView.getMyLocation()
//                    }
//                });

        // TODO daniel, go to my location, move FAB button if possible, span to events
    }
}
