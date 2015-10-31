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

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.joanzapata.iconify.fonts.IoniconsModule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityEventDetails extends AppCompatActivity {

    public static final String EXTRA_ID = "event_id";

    @Bind(R.id.backdrop)
    ImageView backdropImage;

    @Bind(R.id.fab_add_to_saved)
    FloatingActionButton saveEvent;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @Bind(R.id.event_details_start_date)
    TextView textViewEventStartDate;

    @Bind(R.id.event_details_end_date)
    TextView textViewEventEndDate;

    @Bind(R.id.event_location)
    TextView textViewEventLocation;

    @Bind(R.id.event_details)
    TextView textViewEventDetails;

    private boolean isEventSaved = false;
    private Drawable emptyHeart;
    private Drawable filledHeart;

    private Event event;

    static {
        Iconify.with(new IoniconsModule());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        ButterKnife.bind(this);

        emptyHeart = new IconDrawable(this, IoniconsIcons.ion_android_favorite_outline).colorRes(android.R.color.white).actionBarSize();
        filledHeart = new IconDrawable(this, IoniconsIcons.ion_android_favorite).colorRes(R.color.colorPrimary).actionBarSize();

        final Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBare = getSupportActionBar();
        if (actionBare != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // TODO make the toolbar disappear completely on top most scroll

        Intent intent = getIntent();
        final long eventId = intent.getLongExtra(EXTRA_ID, 0);
        event = EventsDatabase.getInstance().getEventById(eventId);

        collapsingToolbar.setTitle(event.name);

        // TODO daniel implement picture uri as picture
        Glide.with(this).load(R.drawable.party_image).centerCrop().into(backdropImage);

        if (event.isEventSaved) {
            saveEvent.setImageDrawable(filledHeart);
        } else {
            saveEvent.setImageDrawable(emptyHeart);
        }

        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
        textViewEventStartDate.setText(dateFormat.format(event.startTimeStamp));
        textViewEventEndDate.setText(dateFormat.format(event.endTimeStamp));

        textViewEventLocation.setText(event.locationString);

        textViewEventDetails.setText(event.details);
//        textViewEventDetails.setMovementMethod(LinkMovementMethod.getInstance());

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.event_detail_map);
        mapFragment.getMapAsync(googleMap -> {
            GoogleMap mapView = googleMap;
            mapView.setMyLocationEnabled(true);
            mapView.getUiSettings().setAllGesturesEnabled(false);
            mapView.getUiSettings().setMyLocationButtonEnabled(false);
            mapView.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(event.location.latitude, event.location.longitude), 15));
            mapView.setOnMapClickListener(latLng -> {
                // TODO daniel open ActivityMap with my location, events location, and a path connecting
            });
            mapView.addMarker(new MarkerOptions().position(new LatLng(event.location.latitude, event.location.longitude)));
        });

    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @OnClick(R.id.fab_add_to_saved)
    public void saveEvent(View view) {
        if (!isEventSaved) {
            isEventSaved = true;
            EventsDatabase.getInstance().persistEvent(event);
            ((FloatingActionButton) findViewById(R.id.fab_add_to_saved)).setImageDrawable(filledHeart);
        } else {
            isEventSaved = false;
            EventsDatabase.getInstance().removeSavedEvent(event);
            ((FloatingActionButton) findViewById(R.id.fab_add_to_saved)).setImageDrawable(emptyHeart);
        }

        Snackbar.make(view,
                isEventSaved ?
                        String.format(getResources().getString(R.string.event_is_saved), event.name) :
                        String.format(getResources().getString(R.string.event_is_removed), event.name),
                Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }
}
