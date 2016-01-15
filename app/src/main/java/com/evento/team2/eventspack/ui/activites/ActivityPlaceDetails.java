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
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.models.Place;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.evento.team2.eventspack.provider.FetchAsyncTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.IoniconsModule;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivityPlaceDetails extends AppCompatActivity {

    public static final String EXTRA_ID = "place_id";

    @Bind(R.id.backdrop)
    ImageView backdropImage;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @Bind(R.id.event_location)
    TextView textViewEventLocation;

    @Bind(R.id.place_details_events_linear_layout)
    LinearLayout placeDetailsEventsLinearLayout;

    private Place place;

    static {
        Iconify.with(new IoniconsModule());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        ButterKnife.bind(this);

        final Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBare = getSupportActionBar();
        if (actionBare != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // TODO make the toolbar disappear completely on top most scroll

        Intent intent = getIntent();
        final long eventId = intent.getLongExtra(EXTRA_ID, 0);
        place = EventsDatabase.getInstance().getPlaceById(eventId);

        collapsingToolbar.setTitle(place.name);

        Glide.with(this).load(R.drawable.place_image).centerCrop().into(backdropImage);

        textViewEventLocation.setText(place.locationString);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.event_detail_map);
        mapFragment.getMapAsync(googleMap -> {
            GoogleMap mapView = googleMap;
            mapView.setMyLocationEnabled(true);
            mapView.getUiSettings().setAllGesturesEnabled(false);
            mapView.getUiSettings().setMyLocationButtonEnabled(false);
            if (place.location.latitude != 0 || place.location.longitude != 0) {
                mapView.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.location.latitude, place.location.longitude), 15));
                mapView.setOnMapClickListener(latLng -> {
                    Intent intentActivityMap = ActivityMap.createIntent(ActivityPlaceDetails.this, FetchAsyncTask.PLACES, place.id);
                    startActivity(intentActivityMap);
                    finish();
                });
                mapView.addMarker(new MarkerOptions().position(new LatLng(place.location.latitude, place.location.longitude)));
            }
        });

        new Thread() {
            @Override
            public void run() {
                ArrayList<Event> eventArrayList = EventsDatabase.getInstance().getEventsByLocation(String.valueOf(place.location.latitude),
                        String.valueOf(place.location.longitude), place.locationString, String.valueOf(new Date().getTime()));

                if (eventArrayList != null && eventArrayList.size() > 0) {
                    for (final Event event : eventArrayList) {
                        final View eventItemView = LayoutInflater.from(ActivityPlaceDetails.this).inflate(R.layout.item_small, placeDetailsEventsLinearLayout, false);
                        ImageView calendarEventImageView = ButterKnife.findById(eventItemView, R.id.small_event_picture);
                        ButterKnife.findById(eventItemView, R.id.event_color).setVisibility(View.GONE);
                        ((TextView) ButterKnife.findById(eventItemView, R.id.event_title)).setText(event.name);
                        ((TextView) ButterKnife.findById(eventItemView, R.id.event_details)).setText(event.details);
                        ((TextView) ButterKnife.findById(eventItemView, R.id.event_time)).setText(event.startTimeString);
                        eventItemView.setClickable(true);
                        eventItemView.setOnClickListener(v -> {
                            Intent eventIntent = ActivityEventDetails.createIntent(ActivityPlaceDetails.this, event.id);
                            startActivity(eventIntent);
                            finish();
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
        }.start();
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

    public static Intent createIntent(Context context, long id) {
        Intent intent = new Intent(context, ActivityPlaceDetails.class);
        intent.putExtra(ActivityPlaceDetails.EXTRA_ID, id);

        return intent;
    }
}
