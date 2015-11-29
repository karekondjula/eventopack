package com.evento.team2.eventspack;

import android.app.Application;
import android.content.Context;
import android.location.Geocoder;

import com.evento.team2.eventspack.provider.EventsDatabase;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.Locale;

/**
 * Created by Daniel on 26-Nov-15.
 */
public class EventiApplication extends Application {

    public static Context applicationContext;

//    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = getBaseContext();
        initSingletons();

        // TODO REMOVEEEEEEEEEE BEFOREEEEEEEEEEEE RELEASE!!!!!!!!!
//        refWatcher = LeakCanary.install(this);
    }

    protected void initSingletons() {
        // Initialize the instance of MySingleton

        EventsDatabase.getInstance().openEventsDatabase(this);
        EventsDatabase.getInstance().setGeocoder(new Geocoder(this, Locale.getDefault()));
    }

//    public static RefWatcher getRefWatcher(Context context) {
//        EventiApplication application = (EventiApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }
}