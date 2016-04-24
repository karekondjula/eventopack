package com.evento.team2.eventspack.ui.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

import com.bumptech.glide.Glide;
import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.components.AppComponent;
import com.evento.team2.eventspack.components.DaggerMapComponent;
import com.evento.team2.eventspack.components.MapComponent;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.models.Place;
import com.evento.team2.eventspack.modules.MapModule;
import com.evento.team2.eventspack.presenters.interfaces.FragmentMapPresenter;
import com.evento.team2.eventspack.ui.activites.ActivityEventDetails;
import com.evento.team2.eventspack.ui.activites.ActivityMap;
import com.evento.team2.eventspack.ui.activites.ActivityPlaceDetails;
import com.evento.team2.eventspack.ui.fragments.interfaces.BaseFragment;
import com.evento.team2.eventspack.utils.DateFormatterUtils;
import com.evento.team2.eventspack.utils.EventiConstants;
import com.evento.team2.eventspack.views.FragmentMapView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Daniel on 29-Oct-15.
 */
@RuntimePermissions
public class FragmentMap extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnMyLocationChangeListener, FragmentMapView {

    public static final String EXTRA_WHAT = "extra_what";
    public static final String EXTRA_ID = "extra_id";
    public static final String TAG = "FragmentMapTag";

    @Inject
    FragmentMapPresenter fragmentMapPresenter;

    @Inject
    CaldroidFragment dialogCaldroidFragment;

    private Location myLocation;
    private Calendar calendar;
    private GoogleMap mapView;
    private String lastSelectedDate;
    private LinearLayout actionViewCalendar;
    private long id = EventiConstants.NONE;
    private int what = EventiConstants.EVENTS;

    @Bind(R.id.map_event_details)
    LinearLayout mapEventDetailsLinearLayout;
    private Snackbar fetchingEventsSnackBar;

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

        // get the two extras and set correct category and date
        Bundle bundle = getArguments();

        if (bundle != null) {
            what = bundle.getInt(EXTRA_WHAT);
            id = bundle.getLong(EXTRA_ID);
        }

        Spinner spinner = ButterKnife.findById(getActivity(), R.id.spinner_navigation);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            boolean annoyingFirstEnter = true;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (annoyingFirstEnter) {
                    // FIXME find the reason behind this
                    annoyingFirstEnter = false;
                    return;
                }

                lastSelectedDate = String.valueOf(calendar.getTimeInMillis());

                switch (i) {
                    case 0:
                        what = EventiConstants.EVENTS;
                        // lastSelectedDate is a full time stamp
                        fragmentMapPresenter.fetchEvents(lastSelectedDate);
                        break;
                    case 1:
                        what = EventiConstants.PLACES;
                        // places do not have time acknowlegment
                        fragmentMapPresenter.fetchPlaces();
                        break;
                    case 2:
                        what = EventiConstants.SAVED_EVENTS;
                        // saved events have a DD.MM.YYYY way of filtering, TODO make this same with EVENTS
                        fragmentMapPresenter.fetchSavedEvents(Long.parseLong(lastSelectedDate));
                        break;
                    default:
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        calendar = Calendar.getInstance();
        setCalendarOnStartOfDay(calendar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_navigation_labes, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (what != EventiConstants.NONE) {
            spinner.setSelection(what);
        }

        bundle = new Bundle();
        bundle.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        dialogCaldroidFragment.setArguments(bundle);
        dialogCaldroidFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                setCalendarDate(date);

                calendar.setTimeInMillis(date.getTime());
                setCalendarOnStartOfDay(calendar);

                lastSelectedDate = String.valueOf(calendar.getTimeInMillis());

                if (what == EventiConstants.EVENTS) {
                    // lastSelectedDate is a full time stamp
                    fragmentMapPresenter.fetchEvents(lastSelectedDate);
                } else if (what == EventiConstants.PLACES) {
                    // places do not have time acknowlegment
                    fragmentMapPresenter.fetchPlaces();
                } else if (what == EventiConstants.SAVED_EVENTS) {
                    // saved events have a DD.MM.YYYY way of filtering, TODO make this same with EVENTS
                    fragmentMapPresenter.fetchSavedEvents(calendar.getTimeInMillis());
                }

                dialogCaldroidFragment.dismiss();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentMapPresenter.setView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        calendar = Calendar.getInstance();
        setCalendarOnStartOfDay(calendar);

        FragmentMapPermissionsDispatcher.initMapWithCheck(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (mapView != null) {
            mapView.clear();
            mapView.stopAnimation();
            mapView = null;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_map, menu);
        final MenuItem calendarMenuItem = menu.findItem(R.id.action_calendar);
        actionViewCalendar = ButterKnife.findById(MenuItemCompat.getActionView(calendarMenuItem), R.id.menu_map_date);
        actionViewCalendar.setOnClickListener(v -> onOptionsItemSelected(calendarMenuItem));

        setCalendarDate(calendar.getTime());
    }

    @Override
    public void filterList(String query) {
        // NO op
    }

    @Override
    protected void injectComponent(AppComponent component) {
        MapComponent mapComponent = DaggerMapComponent.builder()
                .appComponent(((EventiApplication) getActivity().getApplication()).getAppComponent())
                .mapModule(new MapModule())
                .build();

        mapComponent.inject(this);
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
        mapView.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapView.setMyLocationEnabled(true);
        mapView.getUiSettings().setCompassEnabled(true);
        mapView.getUiSettings().setRotateGesturesEnabled(true);
        mapView.getUiSettings().setZoomControlsEnabled(true);
        mapView.getUiSettings().setZoomGesturesEnabled(true);
        mapView.setOnMarkerClickListener(this);
        mapView.setOnMapClickListener(this);
        mapView.setOnMyLocationChangeListener(this);

        if (id == EventiConstants.NO_EVENT_ID) {
            // regular opening of fragment map
            lastSelectedDate = String.valueOf(calendar.getTimeInMillis());
            fragmentMapPresenter.fetchEvents(lastSelectedDate);
        } else {
            if (what == EventiConstants.PLACES) {
                // the user came here by clicking a Place map
                // at the moment no places are connected with dates
                lastSelectedDate = "";
                fragmentMapPresenter.goToPlace(id);
            } else if (what == EventiConstants.EVENTS) {
                // the user came here by clicking an Event map
                Event event = fragmentMapPresenter.goToEvent(id);
                lastSelectedDate = String.valueOf(event.startTimeStamp);

                calendar = Calendar.getInstance();
                if (event.startTimeStamp >= calendar.getTimeInMillis()) {
                    // the event has started in the past so no point to set calendar to past, going to today
                    calendar.setTimeInMillis(event.startTimeStamp);
                }
                setCalendarOnStartOfDay(calendar);
                setCalendarDate(calendar.getTime());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FragmentMapPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    protected void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.location_map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.location_map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
    }

    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    protected void showMapsRationale(final PermissionRequest request) {
        // E.g. show a dialog explaining why you need the permission.
        // Call proceed() or cancel() on the incoming request to continue or abort the current permissions process
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.map_needs_permission)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> request.proceed())
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> request.cancel())
                .show();
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    protected void mapsDenied() {
        // maybe close map activity or just don't show maps?
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    protected void showNeverAskForMap() {
//        Toast.makeText(this, R.string.permission_camera_neverask, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        fragmentMapPresenter.markerClicked(marker, what);
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        removeSelectedEventLayout();
        fragmentMapPresenter.markerClicked(null, what);
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (myLocation == null && id == EventiConstants.NONE) {
            // my location is centered
            moveCamera(location.getLatitude(), location.getLongitude());
            // go to my location only the first time
            myLocation = location;
        }
    }

    private void moveCamera(double latitude, double longitude) {
        LatLngBounds radius = new LatLngBounds(new LatLng(latitude, longitude), new LatLng(latitude, longitude));
        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(radius.getCenter(), 14);
        mapView.animateCamera(center, 1000, null);
    }

    private void removeSelectedEventLayout() {
        mapEventDetailsLinearLayout.removeAllViews();
        mapView.setPadding(0, 0, 0, 0);
    }

    public static FragmentMap newInstance(@EventiConstants.SelectedCategory int what, long id) {
        FragmentMap fragmentMap = new FragmentMap();
        if (what != EventiConstants.NONE) {
            Bundle args = new Bundle();
            args.putInt(EXTRA_WHAT, what);
            args.putLong(EXTRA_ID, id);
            fragmentMap.setArguments(args);
        }
        return fragmentMap;
    }

    private void setCalendarDate(Date date) {
        if (actionViewCalendar != null) {
            // probably too early but calendar is already set to the correct date
            ((TextView) actionViewCalendar.findViewById(R.id.menu_text_date)).setText(DateFormatterUtils.compareDateFormat.format(date));
        }
    }

    private void setCalendarOnStartOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    @Override
    public void showFetchingMarkers() {
        mapView.clear();

        if (getView() != null) {
            if (what == EventiConstants.EVENTS || what == EventiConstants.SAVED_EVENTS) {
                fetchingEventsSnackBar = Snackbar.make(getView(), R.string.fetching_events, Snackbar.LENGTH_INDEFINITE);
            } else if (what == EventiConstants.PLACES) {
                fetchingEventsSnackBar = Snackbar.make(getView(), R.string.fetching_places, Snackbar.LENGTH_INDEFINITE);
            }
            fetchingEventsSnackBar.show();
        }
    }

    @Override
    public void showMarkers(ArrayList<MarkerOptions> markerOptionsArrayList) {
        for (MarkerOptions markerOptions : markerOptionsArrayList) {
            mapView.addMarker(markerOptions);
        }

        fetchingEventsSnackBar.dismiss();
    }

    @Override
    public void showEventSelected(Event event) {
        removeSelectedEventLayout();

        final float scale = getActivity().getResources().getDisplayMetrics().density;
        mapView.setPadding(0, 0, 0, (int) (scale * 80)); // rise the zoom controls when an event is clicked

        final View mapEventItemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_small, mapEventDetailsLinearLayout, false);
        ImageView mapEventImageView = ButterKnife.findById(mapEventItemView, R.id.small_event_picture);

        ((TextView) ButterKnife.findById(mapEventItemView, R.id.event_title)).setText(event.name);
        ((TextView) ButterKnife.findById(mapEventItemView, R.id.event_details)).setText(event.details);

        if (event.startTimeStamp - new Date().getTime() < 0) {
            ((TextView) ButterKnife.findById(mapEventItemView, R.id.event_time))
                    .setTextColor(getActivity().getResources().getColor(android.R.color.holo_red_dark));
        }

        ((TextView) ButterKnife.findById(mapEventItemView, R.id.event_time)).setText(DateFormatterUtils.fullDateFormat.format(event.startTimeStamp));

        if (TextUtils.isEmpty(event.pictureUri)) {
            Glide.with(getActivity()).load(R.drawable.party_image).into(mapEventImageView);
        } else {
            Glide.with(getActivity()).load(event.pictureUri).into(mapEventImageView);
        }

        ButterKnife.findById(mapEventItemView, R.id.event_color).setVisibility(View.GONE);
        ButterKnife.findById(mapEventItemView, R.id.close_event).setVisibility(View.VISIBLE);

        mapEventItemView.setClickable(true);

        ButterKnife.findById(mapEventItemView, R.id.close_event).setOnClickListener(view -> removeSelectedEventLayout());
        mapEventItemView.setOnClickListener(v -> {
            Intent intent = ActivityEventDetails.createIntent(getActivity(), event.id);
            startActivity(intent);
        });

        mapEventDetailsLinearLayout.addView(mapEventItemView);
    }

    @Override
    public void showPlaceSelected(Place place) {
        removeSelectedEventLayout();

        final float scale = getActivity().getResources().getDisplayMetrics().density;
        mapView.setPadding(0, 0, 0, (int) (scale * 80)); // rise the zoom controls when an event is clicked

        final View mapEventItemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_small, mapEventDetailsLinearLayout, false);
        ImageView mapEventImageView = ButterKnife.findById(mapEventItemView, R.id.small_event_picture);

        ((TextView) ButterKnife.findById(mapEventItemView, R.id.event_title)).setText(place.name);
        ((TextView) ButterKnife.findById(mapEventItemView, R.id.event_details)).setText("");
        ((TextView) ButterKnife.findById(mapEventItemView, R.id.event_time)).setText("");

        if (TextUtils.isEmpty(place.pictureUri)) {
            Glide.with(getActivity()).load(R.drawable.place_image).into(mapEventImageView);
        } else {
            Glide.with(getActivity()).load(place.pictureUri).into(mapEventImageView);
        }

        ButterKnife.findById(mapEventItemView, R.id.event_color).setVisibility(View.GONE);
        ButterKnife.findById(mapEventItemView, R.id.close_event).setVisibility(View.VISIBLE);

        mapEventItemView.setClickable(true);

        ButterKnife.findById(mapEventItemView, R.id.close_event).setOnClickListener(view -> removeSelectedEventLayout());
        mapEventItemView.setOnClickListener(v -> {

            Intent intent = ActivityPlaceDetails.createIntent(getActivity(), place.id);
            startActivity(intent);
        });

        mapEventDetailsLinearLayout.addView(mapEventItemView);
    }

    @Override
    public void goToLocationAndAddMarker(MarkerOptions markerOptions) {
        moveCamera(markerOptions.getPosition().latitude, markerOptions.getPosition().longitude);
        mapView.addMarker(markerOptions);
    }
}