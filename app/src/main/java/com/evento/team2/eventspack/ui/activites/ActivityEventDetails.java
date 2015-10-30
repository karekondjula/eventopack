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
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.joanzapata.iconify.fonts.IoniconsModule;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityEventDetails extends AppCompatActivity {

    public static final String EXTRA_ID = "event_id";
//    public static final String EXTRA_NAME = "event_name";
//    public static final String EXTRA_PICTURE_URI = "picture_uri";

    @Bind(R.id.backdrop)
    ImageView backdropImage;

    @Bind(R.id.fab_add_to_saved)
    FloatingActionButton addToFavorites;

    private boolean isEventSaved = false;
    private Drawable emptyHeart;
    private Drawable filledHeart;

    static {
        Iconify.with(new IoniconsModule());
    }

    private Event event;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        ButterKnife.bind(this);

        FloatingActionButton printButton = (FloatingActionButton) findViewById(R.id.fab_add_to_saved);
        emptyHeart = new IconDrawable(this, IoniconsIcons.ion_android_favorite_outline).colorRes(android.R.color.white).actionBarSize();
        filledHeart = new IconDrawable(this, IoniconsIcons.ion_android_favorite).colorRes(R.color.colorPrimary).actionBarSize();
        // TODO check if event is saved
        printButton.setImageDrawable(emptyHeart);

        Intent intent = getIntent();
        final long eventId = intent.getLongExtra(EXTRA_ID, 0);
        event = EventsDatabase.getInstance().getEventById(eventId);

        final Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(event.name);

        // TODO daniel implement picture uri as picture
        Glide.with(this).load(R.drawable.party_image).centerCrop().into(backdropImage);
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
                        "JsonEvent is saved" :
                        "JsonEvent is removed from saved events",
                Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }
}
