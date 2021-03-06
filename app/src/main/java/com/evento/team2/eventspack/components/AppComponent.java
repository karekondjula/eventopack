package com.evento.team2.eventspack.components;

/**
 * Created by Daniel on 16-Jan-16.
 */

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.interactors.NotificationsInteractor;
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.modules.AppModule;
import com.evento.team2.eventspack.receivers.CleanUpEventsReceiver;
import com.evento.team2.eventspack.receivers.DailyEventsReminderReceiver;
import com.evento.team2.eventspack.receivers.DownloadEventsReceiver;
import com.evento.team2.eventspack.receivers.NotificationEventsReceiver;
import com.evento.team2.eventspack.receivers.WeeklyEventsReceiver;
import com.evento.team2.eventspack.services.TranslateService;
import com.evento.team2.eventspack.ui.activites.ActivityMain;
import com.evento.team2.eventspack.ui.fragments.FragmentCategories;
import com.evento.team2.eventspack.ui.fragments.FragmentEvents;
import com.evento.team2.eventspack.ui.fragments.FragmentPlaces;
import com.evento.team2.eventspack.ui.fragments.FragmentSavedEvents;
import com.evento.team2.eventspack.utils.interfaces.MainThread;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(EventiApplication eventiApplication);
    void inject(ActivityMain activityMain);

    void inject(FragmentEvents fragmentEvents);
    void inject(FragmentCategories fragmentCategories);
    void inject(FragmentPlaces fragmentPlaces);
    void inject(FragmentSavedEvents fragmentSavedEvents);

    void inject(DownloadEventsReceiver downloadEventsReceiver);
    void inject(CleanUpEventsReceiver cleanUpEventsReceiver);
    void inject(NotificationEventsReceiver notificationEventsReceiver);
    void inject(DailyEventsReminderReceiver dailyEventsReminderReceiver);
    void inject(WeeklyEventsReceiver weeklyEventsReceiver);

    MainThread mainThread();
    DatabaseInteractor databaseInteractor();
    NotificationsInteractor notificationsInteractor();
    TranslateService translateService();
    EventiApplication eventiApplication();
}
