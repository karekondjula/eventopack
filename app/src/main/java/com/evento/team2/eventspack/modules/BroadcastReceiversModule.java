package com.evento.team2.eventspack.modules;

import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.receivers.DailyEventsReminderReceiver;
import com.evento.team2.eventspack.receivers.NotificationEventsReceiver;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by daniel-kareski on 6/8/16.
 */
@Module
public class BroadcastReceiversModule {

    @Provides
    @Singleton
    NotificationEventsReceiver provideNotificationEventsReceiver(DatabaseInteractor databaseInteractor) {
        return new NotificationEventsReceiver(databaseInteractor);
    }

    @Provides
    @Singleton
    DailyEventsReminderReceiver provideDailyEventsReminderReceiver(DatabaseInteractor databaseInteractor) {
        return new DailyEventsReminderReceiver(databaseInteractor);
    }
}
