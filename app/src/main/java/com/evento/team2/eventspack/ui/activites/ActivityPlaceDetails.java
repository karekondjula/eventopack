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
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
import com.evento.team2.eventspack.utils.DateFormatterUtils;
import com.evento.team2.eventspack.utils.EventiConstants;
import com.evento.team2.eventspack.views.FragmentPlaceDetailsView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ActivityPlaceDetails extends AppCompatActivity implements FragmentPlaceDetailsView {

    public static final String EXTRA_ID = "place_id";

    @Inject
    FragmentPlaceDetailsPresenter fragmentPlaceDetailsPresenter;

    @Bind(R.id.backdrop)
    ImageView backdropImage;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @Bind(R.id.event_location)
    TextView textViewEventLocation;

    @Bind(R.id.place_details_events_linear_layout)
    LinearLayout placeDetailsEventsLinearLayout;

    private Place place;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        ButterKnife.bind(this);

        PlaceDetailsComponent placeDetailsComponent = DaggerPlaceDetailsComponent.builder()
                .appComponent(((EventiApplication) getApplication()).getAppComponent())
                .placeDetailsModule(new PlaceDetailsModule())
                .build();
        placeDetailsComponent.inject(this);

        final Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // TODO make the toolbar disappear completely on top most scroll

        fragmentPlaceDetailsPresenter.setView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (placeDetailsEventsLinearLayout != null) {
            placeDetailsEventsLinearLayout.removeAllViews();
        }

        Intent intent = getIntent();
        long placeId = intent.getLongExtra(EXTRA_ID, 0);

        fragmentPlaceDetailsPresenter.fetchPlaceDetails(placeId);
        fragmentPlaceDetailsPresenter.fetchActiveEventsByLocation(placeId);
    }

    @OnClick(R.id.backdrop)
    public void openImage(View view) {

        Intent fullScreenImage = ActivityFullScreenImage.createIntent(this, ActivityFullScreenImage.PLACE_IMAGE, place.pictureUri, place.name);
        startActivity(fullScreenImage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        Fragment fragment = getFragmentManager().findFragmentById(R.id.event_detail_map);
        if (fragment != null) {
            fragment.onDestroyView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ActivityPlaceDetailsPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    protected void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.event_detail_map);
        mapFragment.getMapAsync(googleMap -> {
            googleMap.setMyLocationEnabled(true);
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

    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    protected void showMapsRationale(final PermissionRequest request) {
        // E.g. show a dialog explaining why you need the permission.
        // Call proceed() or cancel() on the incoming request to continue or abort the current permissions process
        new AlertDialog.Builder(this)
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
        ActivityPlaceDetailsPermissionsDispatcher.initMapWithCheck(this);
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
