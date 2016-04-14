package com.evento.team2.eventspack.interactors;

import android.app.AlarmManager;
import android.content.Intent;

import com.evento.team2.eventspack.components.CalendarComponent;
import com.evento.team2.eventspack.interactors.interfaces.AlarmManagerInteractor;
import com.evento.team2.eventspack.interactors.interfaces.NotificationsInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.receivers.NotificationEventsReceiver;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Daniel on 06-Mar-16.
 */
public class NotificationsInteractorImpl implements NotificationsInteractor {

    private final AlarmManagerInteractor alarmManagerInteractor;

    public NotificationsInteractorImpl(AlarmManagerInteractor alarmManagerInteractor) {
        this.alarmManagerInteractor = alarmManagerInteractor;
    }

    @Override
    public void scheduleNotification(Event event) {
        Intent eventDetailsIntent = NotificationEventsReceiver.getIntent(event.id);

        // TODO preference for how long before start time should the notification show (maybe dialog to choose from)
        alarmManagerInteractor.scheduleOneTime(eventDetailsIntent, event.startTimeStamp - AlarmManager.INTERVAL_HOUR);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(event.startTimeStamp);
        calendar.set(Calendar.HOUR, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() > event.startTimeStamp) {
            // TODO aggregate daily notifications
            alarmManagerInteractor.scheduleOneTime(eventDetailsIntent, calendar.getTimeInMillis());
        }
    }

    @Override
    public void removeScheduleNotification(Event event) {
        Intent eventDetailsIntent = NotificationEventsReceiver.getIntent(event.id);
        alarmManagerInteractor.removeScheduledEvent(eventDetailsIntent);
    }
}
