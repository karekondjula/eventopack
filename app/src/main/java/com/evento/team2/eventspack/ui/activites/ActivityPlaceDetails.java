/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.evento.team2.eventspack.ui.activites;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.components.DaggerPlaceDetailsComponent;
import com.evento.team2.eventspack.components.PlaceDetailsComponent;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.models.Place;
import com.evento.team2.eventspack.modules.PlaceDetailsModule;
import com.evento.team2.eventspack.presenters.interfaces.FragmentPlaceDetailsPresenter;
import com.evento.team2.eventspack.utils.AdsUtils;
import com.evento.team2.eventspack.utils.DateFormatterUtils;
import com.evento.team2.eventspack.utils.EventiConstants;
import com.evento.team2.eventspack.utils.Utils;
import com.evento.team2.eventspack.views.FragmentPlaceDetailsView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ActivityPlaceDetails extends AppCompatActivity implements FragmentPlaceDetailsView {

    public static final String EXTRA_ID = "place_id";

    @Inject
    FragmentPlaceDetailsPresenter fragmentPlaceDetailsPresenter;

    @BindView(R.id.backdrop)
    ImageView backdropImage;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.event_location)
    TextView textViewEventLocation;

    @BindView(R.id.place_details_events_linear_layout)
    LinearLayout placeDetailsEventsLinearLayout;

    private Unbinder unbinder;

    private Place place;
    GoogleMap mapView;
    private long placeId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        unbinder = ButterKnife.bind(this);

        PlaceDetailsComponent placeDetailsComponent = DaggerPlaceDetailsComponent.builder()
                .appComponent(((EventiApplication) getApplication()).getAppComponent())
                .placeDetailsModule(new PlaceDetailsModule())
                .build();
        placeDetailsComponent.inject(this);

        AdsUtils.enableAds(ButterKnife.findById(this, R.id.banner_ad_view));

        final Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        fragmentPlaceDetailsPresenter.setView(this);

        placeId = getIntent().getLongExtra(EXTRA_ID, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, EventiConstants.ungrantedPremissions, EventiConstants.PERMISSIONS_REQUEST_CODE);
        }

        if (placeDetailsEventsLinearLayout != null) {
            placeDetailsEventsLinearLayout.removeAllViews();
        }

        fragmentPlaceDetailsPresenter.fetchPlaceDetails(placeId);
        fragmentPlaceDetailsPresenter.fetchActiveEventsByLocation(placeId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.backdrop)
    public void openImage(View view) {
        Utils.openImageInGallery(this, place.pictureUri);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        Fragment fragment = getFragmentManager().findFragmentById(R.id.event_detail_map);
        if (fragment != null) {
            fragment.onDestroyView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case EventiConstants.PERMISSIONS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {

                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            // TODO a permission was not granted - o.O what to do?
                            return;
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mapView.setMyLocationEnabled(true);
                        }
                    });
                }
                // a permission was not granted - o.O what to do?
            }
        }
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.event_detail_map);
        mapFragment.getMapAsync(googleMap -> {
            mapView = googleMap;
            googleMap.getUiSettings().setAllGesturesEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            if (this.place.location.latitude != 0 || this.place.location.longitude != 0) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(this.place.location.latitude, this.place.location.longitude), 15));
                googleMap.setOnMapClickListener(latLng -> {
                    Intent intentActivityMap = ActivityMap.createIntent(ActivityPlaceDetails.this, EventiConstants.PLACES, this.place.id);
                    startActivity(intentActivityMap);
                    finish();
                });
                googleMap.addMarker(new MarkerOptions().position(new LatLng(this.place.location.latitude, this.place.location.longitude)));
            }
        });
    }

    public static Intent createIntent(Context context, long id) {
        Intent intent = new Intent(context, ActivityPlaceDetails.class);
        intent.putExtra(ActivityPlaceDetails.EXTRA_ID, id);

        return intent;
    }

    @Override
    public void showPlace(Place place) {
        this.place = place;

        collapsingToolbar.setTitle(place.name);

        if (TextUtils.isEmpty(place.pictureUri)) {
            Glide.with(this).load(R.drawable.place_image).into(backdropImage);
        } else {
            Glide.with(this).load(place.pictureUri).into(backdropImage);
        }

        textViewEventLocation.setText(place.locationString);

        initMap();
    }

    @Override
    public void showEventsAtPlace(ArrayList<Event> eventArrayList) {
        if (eventArrayList != null && eventArrayList.size() > 0) {
            for (final Event event : eventArrayList) {
                final View eventItemView = LayoutInflater.from(ActivityPlaceDetails.this).inflate(R.layout.item_small, placeDetailsEventsLinearLayout, false);
                ImageView calendarEventImageView = ButterKnife.findById(eventItemView, R.id.small_event_picture);
                ButterKnife.findById(eventItemView, R.id.event_color).setVisibility(View.GONE);
                ((TextView) ButterKnife.findById(eventItemView, R.id.event_title)).setText(event.name);
                ((TextView) ButterKnife.findById(eventItemView, R.id.event_details)).setText(event.details);
                ((TextView) ButterKnife.findById(eventItemView, R.id.event_time)).setText(DateFormatterUtils.fullDateFormat.format(new Date(event.startTimeStamp)));
                eventItemView.setClickable(true);
                eventItemView.setOnClickListener(v -> {
                    Intent eventIntent = ActivityEventDetails.createIntent(ActivityPlaceDetails.this, event.id);
                    startActivity(eventIntent);
                });

                runOnUiThread(() -> {
                    if (TextUtils.isEmpty(event.pictureUri)) {
                        Glide.with(ActivityPlaceDetails.this).load(R.drawable.party_image).into(calendarEventImageView);
                    } else {
                        Glide.with(ActivityPlaceDetails.this).load(event.pictureUri).into(calendarEventImageView);
                    }
                    placeDetailsEventsLinearLayout.addView(eventItemView);
                });
            }
        }
    }
}
