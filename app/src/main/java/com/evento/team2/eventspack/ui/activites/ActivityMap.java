package com.evento.team2.eventspack.ui.activites;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

/**
 * Created by Daniel on 01-Oct-15.
 */
public class ActivityMap extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnMyLocationChangeListener {

    private static final String DELIMITER = ">>";

    private MapFragment supportMapFragment;
    private GoogleMap mapView;
    private Location myLocation;

    @Bind(R.id.map_event_details)
    LinearLayout mapEventDetailsLinearLayout;

    @Bind(R.id.spinner_navigation)
    Spinner spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        final Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(null);
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setTitle("Events map");
            actionBar.setDisplayShowTitleEnabled(false);
        }

        FragmentManager fm = getFragmentManager();
        supportMapFragment = (MapFragment) fm.findFragmentById(R.id.location_map);
        if (supportMapFragment == null) {
            supportMapFragment = MapFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.location_map, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_navigation_labes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_map, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_calendar) {
            // TODO daniel open datepicker and show events for current dates (background thread of course)
        }

        return super.onOptionsItemSelected(item);
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
        mapView.setOnMyLocationChangeListener(this);

        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.party_image);
        Bitmap bhalfsize = Bitmap.createScaledBitmap(b, b.getWidth() / 8, b.getHeight() / 8, false);
        for (Event event : Utils.Helpers.createEvents()) {
            mapView.addMarker(new MarkerOptions()
                    .position(new LatLng(event.location.latitude,
                            event.location.longitude))
                    .title(event.name)
                    .snippet(event.details + DELIMITER + event.startDateString + " " + event.startTimeString)
                    .icon(BitmapDescriptorFactory.fromBitmap(bhalfsize)));
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        removeSelectedEventLayout();

        mapView.setPadding(0, 0, 0, 140); // rise the zoom controls when an event is clicked

        final View mapEventItemView = LayoutInflater.from(ActivityMap.this).inflate(R.layout.item_small_events, mapEventDetailsLinearLayout, false);
        ImageView mapEventImageView = (ImageView) ButterKnife.findById(mapEventItemView, R.id.small_event_picture);
        mapEventImageView.setImageResource(R.drawable.party_image);
//                Glide.with(getActivity()).load(R.drawable.party_image).into(calendarEventImageView);
        ((TextView) ButterKnife.findById(mapEventItemView, R.id.event_title)).setText(marker.getTitle());
        String eventDetails[] = marker.getSnippet().split(DELIMITER);
        ((TextView) ButterKnife.findById(mapEventItemView, R.id.event_details)).setText(eventDetails[0]);
        ((TextView) ButterKnife.findById(mapEventItemView, R.id.event_time)).setText(eventDetails[1]);
        ButterKnife.findById(mapEventItemView, R.id.event_color).setVisibility(View.GONE);
        ButterKnife.findById(mapEventItemView, R.id.close_event).setVisibility(View.VISIBLE);
        mapEventItemView.setClickable(true);
        ButterKnife.findById(mapEventItemView, R.id.close_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeSelectedEventLayout();
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
        removeSelectedEventLayout();
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (myLocation == null) {
            LatLngBounds radius = new LatLngBounds(new LatLng(location.getLatitude(), location.getLongitude()),
                    new LatLng(location.getLatitude(), location.getLongitude()));
            CameraUpdate center = CameraUpdateFactory.newLatLngZoom(radius.getCenter(), 14);
            mapView.animateCamera(center, 1500, null);
            myLocation = location;
        }
    }

    private void removeSelectedEventLayout() {
        mapEventDetailsLinearLayout.removeAllViews();
        mapView.setPadding(0, 0, 0, 0);
    }
}