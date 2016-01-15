package com.evento.team2.eventspack;

import android.app.Application;
import android.content.Context;
import android.location.Geocoder;

import com.evento.team2.eventspack.components.DaggerPresentersComponent;
import com.evento.team2.eventspack.components.PresentersComponent;
import com.evento.team2.eventspack.modules.AppModule;
import com.evento.team2.eventspack.modules.PresentersModule;
import com.evento.team2.eventspack.provider.EventsDatabase;

import java.util.Locale;

/**
 * Created by Daniel on 26-Nov-15.
 */
public class EventiApplication extends Application {

    public static Context applicationContext;

    private PresentersComponent presentersComponent;

//    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = getBaseContext();
        initSingletons();

        presentersComponent = DaggerPresentersComponent.builder()
                .appModule(new AppModule(this))
                .presentersModule(new PresentersModule()) // TODO maybe it is better to make scope addition or with includes like interactorsmodule
                .build();

//        refWatcher = LeakCanary.install(this);
    }

    protected void initSingletons() {
        // Initialize the instance of MySingleton

        EventsDatabase.getInstance().openEventsDatabase(this);
        EventsDatabase.getInstance().setGeocoder(new Geocoder(this, Locale.getDefault()));
    }

    public PresentersComponent getPresentersComponent() {
        return presentersComponent;
    }

//    public static RefWatcher getRefWatcher(Context context) {
//        EventiApplication application = (EventiApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }
}