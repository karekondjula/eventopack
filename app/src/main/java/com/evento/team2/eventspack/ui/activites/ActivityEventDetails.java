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

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.evento.team2.eventspack.provider.FetchAsyncTask;
import com.evento.team2.eventspack.utils.DateFormatterUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.joanzapata.iconify.fonts.IoniconsModule;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

    @Bind(R.id.eventAttending)
    View textViewEventAttending;

    @Bind(R.id.eventAttendingCount)
    TextView textViewEventAttendingCount;

    private Drawable emptyHeart;
    private Drawable filledHeart;
    private GoogleMap mapView;
    private MapFragment mapFragment;
    private MarkerOptions markerOptions;
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
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // TODO make the toolbar disappear completely on top most scroll

        Intent intent = getIntent();
        final long eventId = intent.getLongExtra(EXTRA_ID, 0);
        event = EventsDatabase.getInstance().getEventById(eventId);

        if (event == null || collapsingToolbar == null) {
            // it happened once ... just to be safe
            // TODO show 'Something went wrong ... please try again'
            finish();
        }

        collapsingToolbar.setTitle(event.name);

        if (TextUtils.isEmpty(event.pictureUri)) {
            Glide.with(this).load(R.drawable.party_image).centerCrop().into(backdropImage);
        } else {
            Glide.with(this).load(event.pictureUri).centerCrop().into(backdropImage);
        }

        if (event.isEventSaved) {
            saveEvent.setImageDrawable(filledHeart);
        } else {
            saveEvent.setImageDrawable(emptyHeart);
        }

        textViewEventStartDate.setText(event.startTimeStamp != 0 ? DateFormatterUtils.fullDateFormat.format(event.startTimeStamp) : "");
        textViewEventEndDate.setText(event.endTimeStamp != 0 ? DateFormatterUtils.fullDateFormat.format(event.endTimeStamp) : "");

        textViewEventLocation.setText(event.locationString);

        textViewEventDetails.setText(event.details);

        if (!TextUtils.isEmpty(event.attendingCount)) {
            textViewEventAttendingCount.setText(event.attendingCount);
        } else {
            textViewEventAttending.setVisibility(View.GONE);
        }

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.event_detail_map);
        mapFragment.getMapAsync(googleMap -> {
            mapView = googleMap;
            mapView.setMyLocationEnabled(true);
            mapView.getUiSettings().setAllGesturesEnabled(false);
            mapView.getUiSettings().setMyLocationButtonEnabled(false);
            if (event.location.latitude != 0 || event.location.longitude != 0) {
                mapView.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(event.location.latitude, event.location.longitude), 15));
                mapView.setOnMapClickListener(latLng -> {
                    Intent activityMapIntent = ActivityMap.createIntent(ActivityEventDetails.this, FetchAsyncTask.EVENTS, event.id);
                    startActivity(activityMapIntent);
                    finish();
                });
                markerOptions = new MarkerOptions().position(new LatLng(event.location.latitude, event.location.longitude));
                mapView.addMarker(markerOptions);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        mapFragment = null;
        if (mapView != null) {
            mapView.clear();
            mapView.stopAnimation();
            mapView = null;
        }
        markerOptions = null;
        emptyHeart = null;
        filledHeart = null;
    }

    @OnClick(R.id.fab_add_to_saved)
    public void saveEvent(View view) {
        event.isEventSaved = !event.isEventSaved;
        ((FloatingActionButton) findViewById(R.id.fab_add_to_saved)).setImageDrawable(event.isEventSaved ? filledHeart : emptyHeart);
        EventsDatabase.getInstance().changeSaveEvent(event);

        Snackbar.make(view,
                event.isEventSaved ?
                        String.format(getResources().getString(R.string.event_is_saved), event.name) :
                        String.format(getResources().getString(R.string.event_is_removed), event.name),
                Snackbar.LENGTH_LONG)
                .show();
    }

    @OnClick(R.id.backdrop)
    public void openImage(View view) {

        Intent fullScreenImage = ActivityFullScreenImage.createIntent(this, ActivityFullScreenImage.EVENT_IMAGE,
                event.pictureUri, event.name);
        startActivity(fullScreenImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }

    public static Intent createIntent(Context context, long id) {
        Intent intent = new Intent(context, ActivityEventDetails.class);
        intent.putExtra(ActivityEventDetails.EXTRA_ID, id);

        return intent;
    }
}
