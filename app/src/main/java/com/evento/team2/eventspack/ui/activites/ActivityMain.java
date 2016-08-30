package com.evento.team2.eventspack.ui.activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.ui.fragments.DialogFragmentAbout;
import com.evento.team2.eventspack.ui.fragments.FragmentCategories;
import com.evento.team2.eventspack.ui.fragments.FragmentEvents;
import com.evento.team2.eventspack.ui.fragments.FragmentPlaces;
import com.evento.team2.eventspack.ui.fragments.FragmentSavedEvents;
import com.evento.team2.eventspack.utils.EventiConstants;
import com.evento.team2.eventspack.utils.MaterialShowCase;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.joanzapata.iconify.fonts.IoniconsModule;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class ActivityMain extends AppCompatActivity {

    public static final String ACTION_SAVED_EVENTS = "ACTION_SAVED_EVENTS";

    private static final String SHOWCASE_ID = "1";

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.tabs)
    PagerSlidingTabStrip pagerSlidingTabStrip;

    private Unbinder unbinder;

    private FragmentEvents fragmentEvents;// = FragmentEvents.newInstance();
    private FragmentCategories fragmentCategories;// = FragmentCategories.newInstance();
    private FragmentSavedEvents fragmentSavedEvents;// = FragmentSavedEvents.newInstance();
    private FragmentPlaces fragmentPlaces;// = FragmentPlaces.newInstance();
    private MenuItem searchMenuItem;

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

        if (savedInstanceState != null) {
            // restore fragment categories due to is complicated parent-child adapter
            fragmentCategories = (FragmentCategories) getSupportFragmentManager().getFragment(savedInstanceState, FragmentCategories.TAG);
        } else {
            fragmentCategories = FragmentCategories.newInstance();
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
        setupViewPager(viewPager);

        pagerSlidingTabStrip.setTextColor(getResources().getColor(android.R.color.white));
        pagerSlidingTabStrip.setShouldExpand(true);
        pagerSlidingTabStrip.setViewPager(viewPager);

        presentShowcaseSequence();
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean goToSavedEvents = getIntent().getAction().equals(ACTION_SAVED_EVENTS);

        int SAVED_EVENTS_ORDINAL_NUMBER = 2;

        if (goToSavedEvents) {
            new Thread() {
                @Override
                public void run() {

                    SystemClock.sleep(700);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(SAVED_EVENTS_ORDINAL_NUMBER);
                        }
                    });
                }
            }.start();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, FragmentCategories.TAG, fragmentCategories);
    }

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
                .colorRes(android.R.color.white)
                .actionBarSize());

        menu.findItem(R.id.action_map).setIcon(new IconDrawable(this, IoniconsIcons.ion_map)
                .colorRes(android.R.color.white)
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
            super.onBackPressed();
        }
    }

    public void presentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(3000);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(ActivityMain.this, SHOWCASE_ID);

        sequence.setConfig(config);

        MaterialShowcaseView.Builder baseView;

        baseView = MaterialShowCase.createBaseShowCase(ActivityMain.this);
        baseView.setTarget(ButterKnife.findById(ActivityMain.this, R.id.appbar));
        baseView.setContentText(R.string.showcase_welcome_message);
        sequence.addSequenceItem(baseView.build());

        baseView = MaterialShowCase.createBaseShowCase(ActivityMain.this);
        baseView.setTarget(ButterKnife.findById(ActivityMain.this, R.id.appbar));
        baseView.setContentText(R.string.showcase_toolbar_message);
        sequence.addSequenceItem(baseView.build());

        baseView = MaterialShowCase.createBaseShowCase(ActivityMain.this);
        baseView.setTarget(((ViewGroup) pagerSlidingTabStrip.getChildAt(0)).getChildAt(0));
        baseView.setContentText(R.string.showcase_events_message);
        sequence.addSequenceItem(baseView.build());

        baseView = MaterialShowCase.createBaseShowCase(ActivityMain.this);
        baseView.setTarget(((ViewGroup) pagerSlidingTabStrip.getChildAt(0)).getChildAt(1));
        baseView.setContentText(R.string.showcase_categories_message);
        sequence.addSequenceItem(baseView.build());

        baseView = MaterialShowCase.createBaseShowCase(ActivityMain.this);
        baseView.setTarget(((ViewGroup) pagerSlidingTabStrip.getChildAt(0)).getChildAt(2));
        baseView.setContentText(R.string.showcase_saved_message);
        sequence.addSequenceItem(baseView.build());

        baseView = MaterialShowCase.createBaseShowCase(ActivityMain.this);
        baseView.setTarget(((ViewGroup) pagerSlidingTabStrip.getChildAt(0)).getChildAt(3));
        baseView.setContentText(R.string.showcase_places_message);
        sequence.addSequenceItem(baseView.build());

        sequence.start();
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(fragmentEvents, getString(R.string.events));
        adapter.addFragment(fragmentCategories, getString(R.string.categories));
        adapter.addFragment(fragmentSavedEvents, getString(R.string.saved));
        adapter.addFragment(fragmentPlaces, getString(R.string.places));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
//                        fragmentEvents.filterList(EventiConstants.NO_FILTER_STRING);
//                        fragmentEvents.showLastUpdatedInfo();
                        break;
                    case 1:
                        fragmentCategories.refreshViewIfRequired();
//                        fragmentCategories.filterList(EventiConstants.NO_FILTER_STRING);
                        break;
                    case 2:
//                        fragmentSavedEvents.filterList(EventiConstants.NO_FILTER_STRING);
                        break;
                    case 3:
//                        fragmentPlaces.filterList(EventiConstants.NO_FILTER_STRING);
                        break;
                    default:
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, ActivityMain.class);
        return intent;
    }

    private static class Adapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments = new ArrayList<>();
        private List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    //    @OnClick(R.id.fab)
//    public void fabAction(View view) {
//    maybe use this for creating a new event
//        Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//    }
}
