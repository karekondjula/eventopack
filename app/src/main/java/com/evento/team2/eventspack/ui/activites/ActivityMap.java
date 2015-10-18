package com.evento.team2.eventspack.ui.activites;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.utils.Utils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 01-Oct-15.
 */
public class ActivityMap extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private MapFragment supportMapFragment;
    private GoogleMap mapView;

    @Bind(R.id.map_event_details)
    LinearLayout mapEventDetailsLinearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

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
        mapView.setOnMarkerClickListener(this);
        mapView.setOnMapClickListener(this);

        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.party_image);
        Bitmap bhalfsize = Bitmap.createScaledBitmap(b, b.getWidth() / 8, b.getHeight() / 8, false);
        for (Event event : Utils.Helpers.createEvents()) {
            mapView.addMarker(new MarkerOptions()
                    .position(new LatLng(event.location.latitude,
                            event.location.longitude))
                    .title(event.name)
                    .snippet(event.details)
                    .icon(BitmapDescriptorFactory.fromBitmap(bhalfsize)));
        }

        mapView.getMyLocation();
        mapView.setPadding(0, 0, 0, 50); // rise the zoom controls when an event is clicked
//        SmartLocation.with(getActivity()).location()
//                .oneFix()
//                .start(new OnLocationUpdatedListener() {
//
//                    @Override
//                    public void onLocationUpdated(Location location) {
//                        mapView.getMyLocation()
//                    }
//                });

        // TODO daniel map span to events nearby
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        mapEventDetailsLinearLayout.removeAllViews();

        final View mapEventItemView = LayoutInflater.from(ActivityMap.this).inflate(R.layout.item_small_events, mapEventDetailsLinearLayout, false);
        ImageView mapEventImageView = (ImageView) ButterKnife.findById(mapEventItemView, R.id.small_event_picture);
        mapEventImageView.setImageResource(R.drawable.party_image);
//                Glide.with(getActivity()).load(R.drawable.party_image).into(calendarEventImageView);
        ((TextView) ButterKnife.findById(mapEventItemView, R.id.event_title)).setText(marker.getTitle());
        ((TextView) ButterKnife.findById(mapEventItemView, R.id.event_details)).setText(marker.getSnippet());
        ButterKnife.findById(mapEventItemView, R.id.close_event).setVisibility(View.VISIBLE);
        mapEventItemView.setClickable(true);
        ButterKnife.findById(mapEventItemView, R.id.close_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapEventDetailsLinearLayout.removeAllViews();
            }
        });
        mapEventItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMap.this, ActivityEventDetails.class);
                intent.putExtra(ActivityEventDetails.EXTRA_NAME, marker.getTitle());
                if (!TextUtils.isEmpty("")) {
                    intent.putExtra(ActivityEventDetails.EXTRA_PICTURE_URI, "");
                }
                startActivity(intent);
            }
        });
        mapEventDetailsLinearLayout.addView(mapEventItemView);

        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mapEventDetailsLinearLayout.removeAllViews();
    }
}
