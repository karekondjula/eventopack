package com.evento.team2.eventspack.ui.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.provider.FetchAsyncTask;
import com.evento.team2.eventspack.ui.activites.ActivityEventDetails;
import com.evento.team2.eventspack.ui.activites.ActivityMap;
import com.evento.team2.eventspack.ui.interfaces.ObserverFragment;
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
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 29-Oct-15.
 */
public class FragmentMap extends ObserverFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnMyLocationChangeListener {

    public static final String TAG = "FragmentMap";
    public static final String DELIMITER = "\n";

    private final Calendar calendar = Calendar.getInstance();
    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private GoogleMap mapView;
    private Location myLocation;
    private CaldroidFragment dialogCaldroidFragment;
    private String lastSelectedDate;
    private TextView actionViewCalendar;
    private @FetchAsyncTask.Category int resultType = FetchAsyncTask.EVENTS;

    @Bind(R.id.map_event_details)
    LinearLayout mapEventDetailsLinearLayout;

    static {
        Iconify.with(new FontAwesomeModule());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        final Toolbar toolbar = ButterKnife.findById(getActivity(), R.id.toolbar);
        ((ActivityMap) getActivity()).setSupportActionBar(toolbar);
        final ActionBar actionBar = ((ActivityMap) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(null);
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setTitle("Events map"); // replaced by spinner selector
            actionBar.setDisplayShowTitleEnabled(false);
        }

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.location_map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.location_map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        Spinner spinner = ButterKnife.findById(getActivity(), R.id.spinner_navigation);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {
                    case 0:
                        resultType = FetchAsyncTask.EVENTS;
                        break;
                    case 1:
                        resultType = FetchAsyncTask.PLACES;
                        break;
                    case 2:
                        resultType = FetchAsyncTask.SAVED_EVENTS;
                        break;
                    default:
                }
                fetchAsyncTask = new FetchAsyncTask(FragmentMap.this, resultType, FetchAsyncTask.DO_NOT_FETCH_FROM_SERVER);
                fetchAsyncTask.execute(FetchAsyncTask.FILTER_DATE, lastSelectedDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_navigation_labes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        dialogCaldroidFragment = CaldroidFragment.newInstance("", calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        Bundle bundle = new Bundle();
        bundle.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        dialogCaldroidFragment.setArguments(bundle);
        dialogCaldroidFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                setCalendarDate(date);

                lastSelectedDate = dateFormat.format(date);

                fetchAsyncTask = new FetchAsyncTask(FragmentMap.this, resultType, FetchAsyncTask.DO_NOT_FETCH_FROM_SERVER);
                fetchAsyncTask.execute(FetchAsyncTask.FILTER_DATE, lastSelectedDate);

                dialogCaldroidFragment.dismiss();
            }
        });

        return view;
    }

    private void setCalendarDate(Date date) {
        actionViewCalendar.setText(dateFormat.format(date));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater); we might need this?!

        inflater.inflate(R.menu.menu_map, menu);
        final MenuItem calendarMenuItem = menu.findItem(R.id.action_calendar);
        calendarMenuItem.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_calendar)
                .colorRes(R.color.caldroid_black).actionBarSize());

        actionViewCalendar = (TextView) MenuItemCompat.getActionView(calendarMenuItem);
        actionViewCalendar.setOnClickListener(v -> onOptionsItemSelected(calendarMenuItem));

        // set today's date in the action menu
        setCalendarDate(calendar.getTime());
    }

    @Override
    public void filterEvents(String query) {
        // NO op
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_calendar) {
            dialogCaldroidFragment.show(((ActivityMap) getActivity()).getSupportFragmentManager(), "Calendar");
        }

        return super.onOptionsItemSelected(item);
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

        lastSelectedDate = dateFormat.format(calendar.getTimeInMillis());

        fetchAsyncTask = new FetchAsyncTask(this, FetchAsyncTask.EVENTS, FetchAsyncTask.DO_NOT_FETCH_FROM_SERVER);
        fetchAsyncTask.execute(FetchAsyncTask.FILTER_DATE, lastSelectedDate);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        removeSelectedEventLayout();

        final float scale = getActivity().getResources().getDisplayMetrics().density;
        mapView.setPadding(0, 0, 0, (int)(scale * 80)); // rise the zoom controls when an event is clicked

        final View mapEventItemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_small_events, mapEventDetailsLinearLayout, false);
        ImageView mapEventImageView = ButterKnife.findById(mapEventItemView, R.id.small_event_picture);
        mapEventImageView.setImageResource(R.drawable.party_image);
//                Glide.with(getActivity()).load(R.drawable.party_image).into(calendarEventImageView);
        ((TextView) ButterKnife.findById(mapEventItemView, R.id.event_title)).setText(marker.getTitle());

        String eventDetails[] = marker.getSnippet().split(DELIMITER);
        ((TextView) ButterKnife.findById(mapEventItemView, R.id.event_details)).setText(eventDetails[0]);
        ((TextView) ButterKnife.findById(mapEventItemView, R.id.event_time)).setText(eventDetails[1]);

        ButterKnife.findById(mapEventItemView, R.id.event_color).setVisibility(View.GONE);
        ButterKnife.findById(mapEventItemView, R.id.close_event).setVisibility(View.VISIBLE);

        mapEventItemView.setClickable(true);

        ButterKnife.findById(mapEventItemView, R.id.close_event).setOnClickListener(view -> removeSelectedEventLayout());
        mapEventItemView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ActivityEventDetails.class);
            intent.putExtra(ActivityEventDetails.EXTRA_ID, hashMapLatLngEventId.get(marker.getPosition()));
            startActivity(intent);
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
            mapView.animateCamera(center, 1000, null);
            myLocation = location;
        }
    }

    private void removeSelectedEventLayout() {
        mapEventDetailsLinearLayout.removeAllViews();
        mapView.setPadding(0, 0, 0, 0);
    }

    public static FragmentMap newInstance() {
        FragmentMap fragmentMap = new FragmentMap();
        return fragmentMap;
    }

    HashMap<LatLng, Long> hashMapLatLngEventId = new HashMap<>();

    @Override
    public void update(Observable observable, Object eventArrayList) {

        if (mapView != null) {
            mapView.clear();

            if (eventArrayList instanceof ArrayList) {

                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.party_image);
                Bitmap bhalfsize = Bitmap.createScaledBitmap(b, b.getWidth() / 8, b.getHeight() / 8, false);

                MarkerOptions markerOptions;
                hashMapLatLngEventId = new HashMap<>();
                for (Event event : (ArrayList<Event>) eventArrayList) {
                    if (event.location.latitude != 0 || event.location.longitude != 0) {
                        markerOptions = new MarkerOptions()
                                .position(new LatLng(event.location.latitude,
                                        event.location.longitude))
                                .title(event.name)
                                .snippet(event.details + DELIMITER + event.startDateString + " " + event.startTimeString)
                                .icon(BitmapDescriptorFactory.fromBitmap(bhalfsize));
                        hashMapLatLngEventId.put(markerOptions.getPosition(), event.id);
                        mapView.addMarker(markerOptions);
                    }
                }
            }
        }
    }
}
