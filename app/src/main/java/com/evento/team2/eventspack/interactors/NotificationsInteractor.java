package com.evento.team2.eventspack.interactors;

import android.app.AlarmManager;
import android.content.Intent;

import com.evento.team2.eventspack.interactors.interfaces.AlarmManagerInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.receivers.NotificationEventsReceiver;

import java.util.Calendar;

/**
 * Created by Daniel on 06-Mar-16.
 */
public class NotificationsInteractor {

    private AlarmManagerInteractor alarmManagerInteractor;

    public NotificationsInteractor(AlarmManagerInteractor alarmManagerInteractor) {
        this.alarmManagerInteractor = alarmManagerInteractor;
    }

    public void scheduleNotification(Event event) {
        Calendar calendar = Calendar.getInstance();

        if (calendar.getTimeInMillis() < event.startTimeStamp) {
            Intent eventDetailsIntent = NotificationEventsReceiver.getIntent(event.id);

            // TODO preference for how long before start time should the notification show (maybe dialog to choose from)
            alarmManagerInteractor.scheduleOneTime(eventDetailsIntent, event.startTimeStamp - AlarmManager.INTERVAL_HOUR);
        }
    }

    public void removeScheduleNotification(Event event) {
        Intent eventDetailsIntent = NotificationEventsReceiver.getIntent(event.id);
        alarmManagerInteractor.removeScheduledEvent(eventDetailsIntent);
    }
}
