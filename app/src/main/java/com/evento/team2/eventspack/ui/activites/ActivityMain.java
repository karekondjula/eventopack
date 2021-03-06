package com.evento.team2.eventspack.ui.activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.ui.fragments.DialogFragmentAbout;
import com.evento.team2.eventspack.ui.fragments.FragmentCategories;
import com.evento.team2.eventspack.ui.fragments.FragmentEvents;
import com.evento.team2.eventspack.ui.fragments.FragmentPlaces;
import com.evento.team2.eventspack.ui.fragments.FragmentSavedEvents;
import com.evento.team2.eventspack.utils.EventiConstants;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.joanzapata.iconify.fonts.IoniconsModule;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ActivityMain extends BaseAppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String ACTION_SAVED_EVENTS = "ACTION_SAVED_EVENTS";

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;

    private Unbinder unbinder;

    private FragmentEvents fragmentEvents;// = FragmentEvents.newInstance();
    FragmentCategories fragmentCategories;// = FragmentCategories.newInstance();
    private FragmentSavedEvents fragmentSavedEvents;// = FragmentSavedEvents.newInstance();
    private FragmentPlaces fragmentPlaces;// = FragmentPlaces.newInstance();
    private MenuItem searchMenuItem;

    InterstitialAd mInterstitialAd;

    static {
        Iconify.with(new IoniconsModule());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((EventiApplication) getApplication()).getAppComponent().inject(this);

        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        fragmentEvents = FragmentEvents.newInstance();
        fragmentSavedEvents = FragmentSavedEvents.newInstance();
        fragmentPlaces = FragmentPlaces.newInstance();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen_ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice("eventi:interstitial_ad")
                        .build();

                mInterstitialAd.loadAd(adRequest);
                finish();
            }
        });

        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("eventi:interstitial_ad")
                .build();
        mInterstitialAd.loadAd(adRequest);

        if (savedInstanceState != null) {
            // restore fragment categories due to is complicated parent-child adapter
            fragmentCategories = (FragmentCategories) getSupportFragmentManager().getFragment(savedInstanceState, FragmentCategories.TAG);
        } else {
            fragmentCategories = FragmentCategories.newInstance();
            fragmentCategories.setRetainInstance(true);
        }

        navigationView.getMenu().getItem(0).setIcon(new IconDrawable(this, IoniconsIcons.ion_calendar)
                .colorRes(android.R.color.black)
                .actionBarSize());

        navigationView.getMenu().getItem(1).setIcon(new IconDrawable(this, IoniconsIcons.ion_map)
                .colorRes(android.R.color.black)
                .actionBarSize());

        navigationView.getMenu().getItem(2).setIcon(new IconDrawable(this, IoniconsIcons.ion_information)
                .colorRes(android.R.color.black)
                .actionBarSize());

        navigationView.setNavigationItemSelectedListener(menuItem -> {

            drawerLayout.closeDrawers();

            switch (menuItem.getItemId()) {

//                    case R.id.settings:
//                        Intent intentSettings = new Intent(ActivityMain.this, ActivitySettings.class);
//                        startActivity(intentSettings);
//                        break;
//                    case R.id.social:
//                        Intent intentSocial = new Intent(ActivityMain.this, ActivitySocial.class);
//                        startActivity(intentSocial);
//                        break;
                case R.id.calendar:
                    Intent intentCalendar = ActivityCalendar.createIntent(ActivityMain.this);
                    startActivity(intentCalendar);
                    break;
                case R.id.map:
                    Intent intentMap = ActivityMap.createIntent(ActivityMain.this, EventiConstants.EVENTS, EventiConstants.NO_EVENT_ID);
                    startActivity(intentMap);
                    break;
                case R.id.about:
                    DialogFragmentAbout editNameDialog = new DialogFragmentAbout();
                    editNameDialog.show(getFragmentManager(), getString(R.string.about));
                    break;
                default:
                    break;
            }

            return true;
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        BottomNavigationView bottomNavigationView = ButterKnife.findById(this, R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragmentEvents, fragmentEvents.getTag())
                .commit();
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        // TODO find fragment by tag
////        getSupportFragmentManager().putFragment(outState, FragmentCategories.TAG, fragmentCategories);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        searchMenuItem = menu.findItem(R.id.action_search);

        menu.findItem(R.id.action_calendar).setIcon(new IconDrawable(this, IoniconsIcons.ion_calendar)
                .actionBarSize());

        menu.findItem(R.id.action_map).setIcon(new IconDrawable(this, IoniconsIcons.ion_map)
                .actionBarSize());

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent = null;
        if (id == R.id.action_map) {
            intent = ActivityMap.createIntent(ActivityMain.this, EventiConstants.EVENTS, EventiConstants.NO_EVENT_ID);
        } else if (id == R.id.action_calendar) {
            intent = ActivityCalendar.createIntent(ActivityMain.this);
        }
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        searchMenuItem.collapseActionView();
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawers();
        } else {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        }
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, ActivityMain.class);
        return intent;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment nextFragment;
        String tag;
        switch (item.getItemId()) {
            case R.id.events:
                tag = FragmentEvents.TAG;
                nextFragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (nextFragment == null) {
                    nextFragment = fragmentEvents;
                }
                break;
            case R.id.categories:
                tag = FragmentCategories.TAG;
                nextFragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (nextFragment == null) {
                    nextFragment = fragmentCategories;
                }
                break;
            case R.id.saved:
                tag = FragmentSavedEvents.TAG;
                nextFragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (nextFragment == null) {
                    nextFragment = fragmentSavedEvents;
                }
                break;
            case R.id.places:
                tag = FragmentPlaces.TAG;
                nextFragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (nextFragment == null) {
                    nextFragment = fragmentPlaces;
                }
                break;
            default:
                return false;
        }
        if (nextFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, nextFragment, tag)
                    .commit();
        }

        return true;
    }

}