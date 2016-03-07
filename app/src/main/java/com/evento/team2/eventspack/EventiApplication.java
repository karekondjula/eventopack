package com.evento.team2.eventspack;

import android.app.Application;
import android.content.Context;

import com.evento.team2.eventspack.components.AppComponent;
import com.evento.team2.eventspack.components.DaggerAppComponent;
import com.evento.team2.eventspack.modules.AppModule;

/**
 * Created by Daniel on 26-Nov-15.
 */
public class EventiApplication extends Application {

    // TODO remove this, use the dagger
    public static Context applicationContext;

    private AppComponent appComponent;
//    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = getBaseContext();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

//        refWatcher = LeakCanary.install(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

//    public static RefWatcher getRefWatcher(Context context) {
//        EventiApplication application = (EventiApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }
}