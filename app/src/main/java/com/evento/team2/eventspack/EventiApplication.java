package com.evento.team2.eventspack;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Intent;

import com.evento.team2.eventspack.components.AppComponent;
import com.evento.team2.eventspack.components.DaggerAppComponent;
import com.evento.team2.eventspack.interactors.interfaces.AlarmManagerInteractor;
import com.evento.team2.eventspack.modules.AppModule;
import com.evento.team2.eventspack.receivers.CleanUpEventsReceiver;
import com.evento.team2.eventspack.receivers.DownloadEventsReceiver;
import com.evento.team2.eventspack.receivers.WeeklyEventsReceiver;

import java.util.Date;

import javax.inject.Inject;

/**
 * Created by Daniel on 26-Nov-15.
 */
public class EventiApplication extends Application {

    private static final long NOW = new Date().getTime();

    private AppComponent appComponent;

    @Inject
    AlarmManagerInteractor alarmManagerInteractor;

//    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);

        Intent downloadEventsIntent = DownloadEventsReceiver.getIntent();
        alarmManagerInteractor.scheduleRepeating(downloadEventsIntent, NOW, AlarmManager.INTERVAL_HALF_DAY);

        Intent cleanUpEventsIntent = CleanUpEventsReceiver.getIntent();
        alarmManagerInteractor.scheduleRepeating(cleanUpEventsIntent, NOW, AlarmManager.INTERVAL_DAY * 30);

//        Intent savedEventsIntent = WeeklyEventsReceiver.getIntent();
//        // calculate time to monday
////        Calendar -> set to next monday -> calendar - now
//        alarmManagerInteractor.scheduleRepeating(cleanUpEventsIntent, NOW, AlarmManager.INTERVAL_DAY * 7);

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