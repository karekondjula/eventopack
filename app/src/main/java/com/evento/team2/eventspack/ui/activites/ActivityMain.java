package com.evento.team2.eventspack.ui.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.evento.team2.eventspack.soapservice.ServiceEvento;
import com.evento.team2.eventspack.ui.fragments.FragmentEvents;
import com.evento.team2.eventspack.ui.fragments.FragmentSavedEvents;
import com.evento.team2.eventspack.utils.EventsController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class ActivityMain extends AppCompatActivity {

    @Bind(R.id.navigation_view)
    NavigationView navigationView;
    @Bind(R.id.drawer)
    DrawerLayout drawerLayout;
//    @Bind(R.id.fab)
//    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        EventsDatabase.getInstance().openEventsDatabase(this);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {

                    case R.id.settings:
                        Intent intentSettings = new Intent(ActivityMain.this, ActivitySettings.class);
                        startActivity(intentSettings);
                        break;
                    case R.id.calendar:
                        Intent intentCalendar = new Intent(ActivityMain.this, ActivityCalendar.class);
                        startActivity(intentCalendar);
                        break;
                    case R.id.logout:
                        Toast.makeText(getApplicationContext(), "Log me out :(", Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
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
            setupViewPager(viewPager, EventsController.getInstance().getEventBus());
        }

        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pagerSlidingTabStrip.setTextColor(getResources().getColor(android.R.color.white));
        pagerSlidingTabStrip.setShouldExpand(true);
        pagerSlidingTabStrip.setViewPager(viewPager);

//        Intent intent = new Intent(this, ActivityLogin.class);
//        startActivity(intent);

//        HashMap<String, Object> params = new HashMap<>();
//        params.put(ServiceEvento.METHOD_NAME_KEY, ServiceEvento.METHOD_TEST_FUNC);
//        ServiceEvento.getInstance().callServiceMethod(params);

        HashMap<String, Object> params = new HashMap<>();
        params.put(ServiceEvento.METHOD_NAME_KEY, ServiceEvento.METHOD_GET_ALL_EVENTS);

        ServiceEvento.getInstance().callServiceMethod(params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_map) {
            Intent intent = new Intent(ActivityMain.this, ActivityMap.class);
            startActivity(intent);
        } else if (id == R.id.action_calendar) {
            Intent intentCalendar = new Intent(ActivityMain.this, ActivityCalendar.class);
            startActivity(intentCalendar);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager, EventBus eventBus) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(FragmentEvents.newInstance(), "Events");
        adapter.addFragment(new Fragment(), "Places");
        adapter.addFragment(FragmentSavedEvents.newInstance(eventBus), "Saved");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(android.support.v4.app.FragmentManager fm) {
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
//        Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//    }
}
