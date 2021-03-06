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
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.components.DaggerEventDetailsComponent;
import com.evento.team2.eventspack.components.EventDetailsComponent;
import com.evento.team2.eventspack.interactors.NotificationsInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.modules.EventDetailsModule;
import com.evento.team2.eventspack.presenters.EventDetailsPresenter;
import com.evento.team2.eventspack.utils.AdsUtils;
import com.evento.team2.eventspack.utils.DateFormatterUtils;
import com.evento.team2.eventspack.utils.EventiConstants;
import com.evento.team2.eventspack.utils.Utils;
import com.evento.team2.eventspack.views.FragmentEventDetailsView;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.plus.PlusShare;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.EntypoModule;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.joanzapata.iconify.fonts.IoniconsModule;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ActivityEventDetails extends BaseAppCompatActivity implements FragmentEventDetailsView {

    public static final String EXTRA_EVENT_ID = "event_id";

    @Inject
    EventiApplication eventiApplication;

    @Inject
    EventDetailsPresenter eventDetailsPresenter;

    @Inject
    NotificationsInteractor notificationsInteractor;

    @BindView(R.id.backdrop)
    ImageView backdropImage;

    @BindView(R.id.fab_add_to_saved)
    FloatingActionButton saveEvent;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.event_details_start_date)
    TextView textViewEventStartDate;

    @BindView(R.id.event_details_end_date)
    TextView textViewEventEndDate;

    @BindView(R.id.event_location)
    TextView textViewEventLocation;

    @BindView(R.id.event_details)
    TextView textViewEventDetails;

    @BindView(R.id.event_attending)
    TextView textViewEventAttending;

    @BindView(R.id.bottom_sheet)
    View bottomSheet;

    @BindView(R.id.translate)
    ImageButton translate;

    private Unbinder unbinder;

    private FloatingActionButton fab;

    private Drawable emptyHeart;
    private Drawable filledHeart;
    GoogleMap mapView;
    private MapFragment mapFragment;
    private MarkerOptions markerOptions;
    private Event event;
    private Snackbar snackbar;

    long eventId;

    static {
        Iconify.with(new IoniconsModule());
        Iconify.with(new EntypoModule());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_details);
        unbinder = ButterKnife.bind(this);

        EventDetailsComponent eventDetailsComponent = DaggerEventDetailsComponent.builder()
                .appComponent(((EventiApplication) getApplication()).getAppComponent())
                .eventDetailsModule(new EventDetailsModule())
                .build();
        eventDetailsComponent.inject(this);

        AdsUtils.enableAds(ButterKnife.findById(this, R.id.banner_ad_view));

        emptyHeart = new IconDrawable(this, IoniconsIcons.ion_android_favorite_outline).colorRes(android.R.color.white).actionBarSize();
        filledHeart = new IconDrawable(this, IoniconsIcons.ion_android_favorite).colorRes(R.color.colorPrimary).actionBarSize();

        final Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        fab = ((FloatingActionButton) findViewById(R.id.fab_add_to_saved));

        BottomSheetBehavior mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(dipToPixels(this, 70));
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        eventId = getIntent().getLongExtra(EXTRA_EVENT_ID, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, EventiConstants.ungrantedPremissions, EventiConstants.PERMISSIONS_REQUEST_CODE);
        }

        eventDetailsPresenter.setView(this);

        eventDetailsPresenter.fetchEventDetails(eventId);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case EventiConstants.PERMISSIONS_REQUEST_CODE: {
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

    @OnClick(R.id.fab_add_to_saved)
    public void saveEvent(View view) {
        eventDetailsPresenter.changeSavedStateOfEvent(event);
    }

    @OnClick(R.id.backdrop)
    public void openImage(View view) {
        Utils.openImageInGallery(this, event.pictureUri);
    }

    @OnClick(R.id.share_facebook)
    public void shareOnFacebook(View view) {

        ShareDialog shareDialog = new ShareDialog(this);

        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle(event.name)
                .setContentDescription(event.details)
                .setContentUrl(Uri.parse("http://www.facebook.com/events/".concat(String.valueOf(event.facebookId))))
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag("#евенти")
                        .build())
                .setQuote(getString(R.string.promo_message))
                .build();

        shareDialog.show(linkContent);
    }

    @OnClick(R.id.share_gplus)
    public void shareOnGplus(View view) {

        Intent shareIntent = new PlusShare.Builder(this)
                .setType("text/plain")
                .setText(getString(R.string.promo_message))
                .setContentUrl(Uri.parse("http://www.facebook.com/events/".concat(String.valueOf(event.facebookId))))
                .getIntent();

        startActivityForResult(shareIntent, 0);
    }

    @OnClick(R.id.share_twitter)
    public void shareOnTwitter(View view) {

        String tweetUrl = "https://twitter.com/intent/tweet?text="
                + getString(R.string.promo_message) + " at "
                + "http://www.facebook.com/events/".concat(String.valueOf(event.facebookId));
        Uri uri = Uri.parse(tweetUrl);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @OnClick(R.id.share_on_other)
    public void shareOnOther(View view) {

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/*");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                "http://www.facebook.com/events/".concat(String.valueOf(event.facebookId)));
        startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }

    @OnClick(R.id.translate)
    public void translate(View view) {
        // TODO show license for translation
        if (translated) {
            setTranslatedDetails(event.details);
        } else {
            eventDetailsPresenter.translateToEnglish(event);
        }
    }

    private void initMap() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.event_detail_map);
        mapFragment.getMapAsync(googleMap -> {
            mapView = googleMap;
            mapView.getUiSettings().setAllGesturesEnabled(false);
            mapView.getUiSettings().setMyLocationButtonEnabled(false);

            if ((event.location.latitude != 0 || event.location.longitude != 0)) {
                mapView.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(event.location.latitude, event.location.longitude), 15));
                mapView.setOnMapClickListener(latLng -> {
                    Intent activityMapIntent = ActivityMap.createIntent(ActivityEventDetails.this, EventiConstants.EVENTS, event.id);
                    startActivity(activityMapIntent);
                    finish();
                });
                markerOptions = new MarkerOptions().position(new LatLng(event.location.latitude, event.location.longitude));
                mapView.addMarker(markerOptions);
            }
        });
    }

    @Override
    public void showEvent(Event event) {
        this.event = event;

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
            textViewEventAttending.setText(getString(R.string.attending, event.attendingCount));
        } else {
            textViewEventAttending.setVisibility(View.GONE);
        }

        initMap();
    }

    @Override
    public void notifyUserForSavedEvent(boolean isSaved) {
        fab.setImageDrawable(event.isEventSaved ? filledHeart : emptyHeart);

        if (event.isEventSaved) {
            Snackbar.make(bottomSheet,
                    getString(R.string.event_is_saved),
                    Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    private boolean translated = false;

    @Override
    public void setTranslatedDetails(String details) {
        textViewEventDetails.setText(details);
        translated = !translated;
    }

    private static int dipToPixels(Context context, int dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    @Override
    public void showTranslatingMessage() {
        snackbar = Snackbar.make(collapsingToolbar,
                R.string.translating_text,
                Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }

    @Override
    public void dismissTranslatingMessage() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    public static Intent createIntent(Context context, long id) {
        Intent intent = new Intent(context, ActivityEventDetails.class);
        intent.putExtra(ActivityEventDetails.EXTRA_EVENT_ID, id);

        return intent;
    }
}