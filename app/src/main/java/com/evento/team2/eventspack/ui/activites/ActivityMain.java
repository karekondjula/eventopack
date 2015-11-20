package com.evento.team2.eventspack.ui.activites;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v13.app.FragmentPagerAdapter;
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

import com.astuetz.PagerSlidingTabStrip;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.evento.team2.eventspack.ui.fragments.DialogFragmentAbout;
import com.evento.team2.eventspack.ui.fragments.FragmentEvents;
import com.evento.team2.eventspack.ui.fragments.FragmentPlaces;
import com.evento.team2.eventspack.ui.fragments.FragmentSavedEvents;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.joanzapata.iconify.fonts.IoniconsModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivityMain extends AppCompatActivity {

    @Bind(R.id.navigation_view)
    NavigationView navigationView;
    @Bind(R.id.drawer)
    DrawerLayout drawerLayout;

    private FragmentEvents fragmentEvents = FragmentEvents.newInstance();
    private FragmentSavedEvents fragmentSavedEvents = FragmentSavedEvents.newInstance();
    private FragmentPlaces fragmentPlaces = FragmentPlaces.newInstance();
    private MenuItem searchMenuItem;

    static {
        Iconify.with(new IoniconsModule());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        EventsDatabase.getInstance().openEventsDatabase(this);
        EventsDatabase.getInstance().setGeocoder(new Geocoder(this, Locale.getDefault()));

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

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
                case R.id.about:
                    DialogFragmentAbout editNameDialog = new DialogFragmentAbout();
                    editNameDialog.show(getFragmentManager(), "about");
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

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        ViewPager viewPager = ButterKnife.findById(this, R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pagerSlidingTabStrip.setTextColor(getResources().getColor(android.R.color.white));
        pagerSlidingTabStrip.setShouldExpand(true);
        pagerSlidingTabStrip.setViewPager(viewPager);

//        HashMap<String, Object> params = new HashMap<>();
//        params.put(ServiceEvento.METHOD_NAME_KEY, ServiceEvento.METHOD_TEST_FUNC);
//        ServiceEvento.getInstance().callServiceMethod(params);

        Intent intent = getIntent();
        if (intent != null && Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            switch (viewPager.getCurrentItem()) {
                case 0:
                    fragmentEvents.filterList(query);
                    break;
                case 1:
                    fragmentPlaces.filterList(query);
                    break;
                case 2:
                    fragmentSavedEvents.filterList(query);
                    break;
                default:
            }
        }
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        searchMenuItem = menu.findItem(R.id.action_search);

        menu.findItem(R.id.action_calendar).setIcon(
                new IconDrawable(this, IoniconsIcons.ion_calendar)
                        .colorRes(android.R.color.white)
                        .actionBarSize());

        menu.findItem(R.id.action_map).setIcon(
                new IconDrawable(this, IoniconsIcons.ion_map)
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

        if (id == R.id.action_map) {
            Intent intent = new Intent(ActivityMain.this, ActivityMap.class);
            startActivity(intent);
        } else if (id == R.id.action_calendar) {
            Intent intentCalendar = new Intent(ActivityMain.this, ActivityCalendar.class);
            startActivity(intentCalendar);
        }

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

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getFragmentManager());
        adapter.addFragment(fragmentEvents, getString(R.string.events));
        adapter.addFragment(fragmentPlaces, getString(R.string.places));
        adapter.addFragment(fragmentSavedEvents, getString(R.string.saved));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        fragmentEvents.filterList(null);
                        break;
                    case 1:
                        fragmentPlaces.filterList(null);
                        break;
                    case 2:
                        fragmentSavedEvents.filterList(null);
                        break;
                    default:
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

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
