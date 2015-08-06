package com.evento.team2.eventspack;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Daniel on 05-Aug-15.
 */
public class ActivitySettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar actionbar = (Toolbar) findViewById(R.id.toolbar);
        actionbar.setTitle("Settings");
        actionbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_dashboard));
        actionbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        getFragmentManager().beginTransaction()
                .add(R.id.content_frame, new SettingsPreference(), SettingsPreference.TAG)
                .addToBackStack(SettingsPreference.TAG)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (getFragmentManager().getBackStackEntryCount() == 1) {
            onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }

        return true;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(mListener);
//    }
//
//    @Override
//    public void onPause() {
////        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mListener);
//        super.onPause();
//    }

    public static class SettingsPreference extends PreferenceFragment {

        public static final String TAG = SettingsPreference.class.getSimpleName();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            // applyTheme();
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_fragment_settings);

            Toolbar actionbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            actionbar.setTitle("Settings");
            actionbar.setNavigationIcon(getResources().getDrawable(android.R.drawable.ic_menu_revert));
            actionbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            });
            ((AppCompatActivity) getActivity()).setSupportActionBar(actionbar);
        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

            String nextFragment = preference.getFragment().toString();
            Fragment nextFragmentSettings = null;
            if (nextFragment.equals(NotificationSettingsFragment.NAME)) {
                nextFragmentSettings = new NotificationSettingsFragment();
            } else if (nextFragment.equals(FindEventsSettingsFragment.NAME)) {
                nextFragmentSettings = new FindEventsSettingsFragment();
            }

            getFragmentManager().beginTransaction()
                    .add(R.id.content_frame, nextFragmentSettings)
                    .addToBackStack(nextFragment)
                    .commit();

            getFragmentManager().beginTransaction().hide(this).commit();

            return true;
        }
    }

    public static class NotificationSettingsFragment extends PreferenceFragment {

        public static final String NAME = NotificationSettingsFragment.class.getName();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Toolbar actionbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            actionbar.setTitle("NotificationSettingsFragment");
            actionbar.setNavigationIcon(getResources().getDrawable(android.R.drawable.ic_menu_revert));
            actionbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            });
            ((AppCompatActivity) getActivity()).setSupportActionBar(actionbar);

            addPreferencesFromResource(R.xml.pref_notification_settings);
        }

        @Override
        public void onDetach() {
            super.onDetach();
            Fragment settingsFragment = getFragmentManager().findFragmentByTag(SettingsPreference.TAG);
            if (settingsFragment != null) {
                getFragmentManager().beginTransaction().show(settingsFragment).commit();
            }
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    public static class FindEventsSettingsFragment extends PreferenceFragment {

        public static final String NAME = FindEventsSettingsFragment.class.getName();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Toolbar actionbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            actionbar.setTitle("FindEventsSettingsFragment");
            actionbar.setNavigationIcon(getResources().getDrawable(android.R.drawable.ic_menu_revert));
            actionbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            });
            ((AppCompatActivity) getActivity()).setSupportActionBar(actionbar);

            addPreferencesFromResource(R.xml.pref_fragment_find_events);
            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
//            bindPreferenceSummaryToValue((Preference) findPreference("notifications_new_message_ringtone"));
        }

        @Override
        public void onDetach() {
            super.onDetach();
            Fragment settingsFragment = getFragmentManager().findFragmentByTag(SettingsPreference.TAG);
            if (settingsFragment != null) {
                getFragmentManager().beginTransaction().show(settingsFragment).commit();
            }
        }
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(android.preference.Preference preference, Object value) {
            String stringValue = value.toString();
            return false;
        }
    };

    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
//        if (preference instanceof MultiSelectListPreference) {
//            String summary = SharedPreferencesCompat.getStringSet(
//                    PreferenceManager.getDefaultSharedPreferences(preference.getContext()),
//                    preference.getKey(),
//                    new HashSet<String>())
//                    .toString();
//            summary = summary.trim().substring(1, summary.length() - 1); // strip []
//            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, summary);
//        } else {
//            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
//                    PreferenceManager
//                            .getDefaultSharedPreferences(preference.getContext())
//                            .getString(preference.getKey(), ""));
//        }
    }
}