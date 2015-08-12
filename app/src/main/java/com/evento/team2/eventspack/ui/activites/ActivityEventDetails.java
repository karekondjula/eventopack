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

package com.evento.team2.eventspack.ui.fragments.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.evento.team2.eventspack.R;

import butterknife.ButterKnife;

public class ActivityEventDetails extends AppCompatActivity {

    public static final String EXTRA_NAME = "event_name";
    public static final String EXTRA_PICTURE_URI = "picture_uri";

//    private ExitActivityTransition exitTransition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
//        ButterKnife.bind(this);

        // TODO daniel find better looking transition (do not forget to remove the library)
//        exitTransition = ActivityTransition.with(getIntent()).to(findViewById(R.id.backdrop)).start(savedInstanceState);

        Intent intent = getIntent();
        final String eventName = intent.getStringExtra(EXTRA_NAME);
        final String eventPictureUri = intent.getStringExtra(EXTRA_PICTURE_URI);

        final Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(eventName);

        loadBackdrop(eventPictureUri);
    }

    private void loadBackdrop(String pictureUri) {
        // TODO daniel implement picture uri as picture
        final ImageView imageView = ButterKnife.findById(this, R.id.backdrop);
        Glide.with(this).load(R.drawable.party_image).centerCrop().into(imageView);
    }

//    @Override
//    public void onBackPressed() {
//        exitTransition.exit(this);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }
}
