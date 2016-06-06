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
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.components.DaggerEventDetailsComponent;
import com.evento.team2.eventspack.components.EventDetailsComponent;
import com.evento.team2.eventspack.interactors.interfaces.NotificationsInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.modules.EventDetailsModule;
import com.evento.team2.eventspack.presenters.interfaces.FragmentEventDetailsPresenter;
import com.evento.team2.eventspack.utils.DateFormatterUtils;
import com.evento.team2.eventspack.utils.EventiConstants;
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
public class ActivityEventDetails extends AppCompatActivity implements FragmentEventDetailsView {

    public static final String EXTRA_ID = "event_id";

    @Inject
    FragmentEventDetailsPresenter fragmentEventDetailsPresenter;

    @Inject
    NotificationsInteractor notificationsInteractor;

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

    @Bind(R.id.bottom_sheet)
    View bottomSheet;

    private FloatingActionButton fab;

    private Drawable emptyHeart;
    private Drawable filledHeart;
    private GoogleMap mapView;
    private MapFragment mapFragment;
    private MarkerOptions markerOptions;
    private Event event;

    long eventId;

    static {
        Iconify.with(new IoniconsModule());
        Iconify.with(new EntypoModule());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_details);
        ButterKnife.bind(this);

        EventDetailsComponent eventDetailsComponent = DaggerEventDetailsComponent.builder()
                .appComponent(((EventiApplication) getApplication()).getAppComponent())
                .eventDetailsModule(new EventDetailsModule())
                .build();
        eventDetailsComponent.inject(this);

        emptyHeart = new IconDrawable(this, IoniconsIcons.ion_android_favorite_outline).colorRes(android.R.color.white).actionBarSize();
        filledHeart = new IconDrawable(this, IoniconsIcons.ion_android_favorite).colorRes(R.color.colorPrimary).actionBarSize();

        final Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // TODO make the toolbar disappear completely on top most scroll

        fab = ((FloatingActionButton) findViewById(R.id.fab_add_to_saved));

        fragmentEventDetailsPresenter.setView(this);

        BottomSheetBehavior mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(dipToPixels(this, 70));
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        eventId = intent.getLongExtra(EXTRA_ID, 0);

        fragmentEventDetailsPresenter.fetchEventDetails(eventId);
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
        fragmentEventDetailsPresenter.updateSavedStateOfEvent(event);

        YoYo.with(Techniques.Tada)
                .duration(700)
                .playOn(view);
    }

    @OnClick(R.id.backdrop)
    public void openImage(View view) {

        // TODO Consider removing this extra activity which we created because we couldn't get Bitmap fro URI
        // TODO this is how it's done
//        Bitmap b = Glide.with(eventiApplication).load(event.pictureUri).
//                asBitmap().into(-1, -1).get();

        Intent fullScreenImage = ActivityFullScreenImage.createIntent(this,
                ActivityFullScreenImage.EVENT_IMAGE, event.pictureUri, event.name);
        startActivity(fullScreenImage);
    }

    @OnClick(R.id.share_facebook)
    public void shareOnFacebook(View view) {

        YoYo.with(Techniques.Tada)
                .duration(700)
                .playOn(view);

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

        YoYo.with(Techniques.Tada)
                .duration(700)
                .playOn(view);

        Intent shareIntent = new PlusShare.Builder(this)
                .setType("text/plain")
                .setText(getString(R.string.promo_message))
                .setContentUrl(Uri.parse("http://www.facebook.com/events/".concat(String.valueOf(event.facebookId))))
                .getIntent();

        startActivityForResult(shareIntent, 0);
    }

    @OnClick(R.id.share_twitter)
    public void shareOnTwitter(View view) {

        YoYo.with(Techniques.Tada)
                .duration(700)
                .playOn(view);

        String tweetUrl = "https://twitter.com/intent/tweet?text="
                + getString(R.string.promo_message) + " at "
                + "http://www.facebook.com/events/".concat(String.valueOf(event.facebookId));
        Uri uri = Uri.parse(tweetUrl);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ActivityEventDetailsPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    protected void initMap() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.event_detail_map);
        mapFragment.getMapAsync(googleMap -> {
            mapView = googleMap;
            mapView.setMyLocationEnabled(true);
            mapView.getUiSettings().setAllGesturesEnabled(false);
            mapView.getUiSettings().setMyLocationButtonEnabled(false);
            if (event.location.latitude != 0 || event.location.longitude != 0) {
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

    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    protected void showMapsRationale(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.map_needs_permission)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> request.proceed())
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> request.cancel())
                .show();
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    protected void mapsDenied() {
        // maybe close menuItemMap activity or just don't show maps?
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    protected void showNeverAskForMap() {
//        Toast.makeText(this, R.string.permission_camera_neverask, Toast.LENGTH_SHORT).show();
    }

    public static Intent createIntent(Context context, long id) {
        Intent intent = new Intent(context, ActivityEventDetails.class);
        intent.putExtra(ActivityEventDetails.EXTRA_ID, id);

        return intent;
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
            textViewEventAttendingCount.setText(event.attendingCount);
        } else {
            textViewEventAttending.setVisibility(View.GONE);
        }

        ActivityEventDetailsPermissionsDispatcher.initMapWithCheck(this);
    }

    @Override
    public void notifyUserForUpdateInEvent(boolean isSaved) {
        fab.setImageDrawable(event.isEventSaved ? filledHeart : emptyHeart);

        Snackbar.make(bottomSheet,
                event.isEventSaved ?
                        String.format(getResources().getString(R.string.event_is_saved), event.name) :
                        String.format(getResources().getString(R.string.event_is_removed), event.name),
                Snackbar.LENGTH_LONG)
                .show();
    }

    private static int dipToPixels(Context context, int dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
}