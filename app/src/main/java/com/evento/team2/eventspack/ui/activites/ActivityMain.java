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
import com.evento.team2.eventspack.provider.FetchAsyncTask;
import com.evento.team2.eventspack.ui.fragments.DialogFragmentAbout;
import com.evento.team2.eventspack.ui.fragments.FragmentEvents;
import com.evento.team2.eventspack.ui.fragments.FragmentPlaces;
import com.evento.team2.eventspack.ui.fragments.FragmentSavedEvents;
import com.evento.team2.eventspack.utils.MaterialShowCase;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.joanzapata.iconify.fonts.IoniconsModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class ActivityMain extends AppCompatActivity {

    private static final String SHOWCASE_ID = "0";

    @Bind(R.id.navigation_view)
    NavigationView navigationView;
    @Bind(R.id.drawer)
    DrawerLayout drawerLayout;
    @Bind(R.id.viewpager)
    ViewPager viewPager;

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

        PagerSlidingTabStrip pagerSlidingTabStrip = ButterKnife.findById(this, R.id.tabs);
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

        presentShowcaseSequence();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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

        Intent intent = null;
        if (id == R.id.action_map) {
            intent = ActivityMap.createIntent(ActivityMain.this, FetchAsyncTask.EVENTS, FetchAsyncTask.NO_EVENT_ID);
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

    private void presentShowcaseSequence() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(2000);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);

        sequence.setConfig(config);

        MaterialShowcaseView.Builder baseView;

        baseView = MaterialShowCase.createBaseShowCase(this);
        baseView.setTarget(viewPager);
        baseView.setContentText(R.string.showcase_welcome_message);
        sequence.addSequenceItem(baseView.build());

        baseView = MaterialShowCase.createBaseShowCase(this);
        baseView.setTarget(ButterKnife.findById(this, R.id.appbar));
        baseView.setContentText(R.string.showcase_toolbar_message);
        sequence.addSequenceItem(baseView.build());

        sequence.start();
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
//                        fragmentEvents.filterList(FetchAsyncTask.NO_FILTER_STRING);
                        break;
                    case 1:
//                        fragmentPlaces.filterList(FetchAsyncTask.NO_FILTER_STRING);
                        break;
                    case 2:
//                        fragmentSavedEvents.filterList(FetchAsyncTask.NO_FILTER_STRING);
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
