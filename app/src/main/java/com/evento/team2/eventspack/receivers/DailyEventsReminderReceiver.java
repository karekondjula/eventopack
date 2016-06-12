package com.evento.team2.eventspack.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.ui.activites.ActivityEventDetails;
import com.evento.team2.eventspack.utils.DateFormatterUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by daniel-kareski on 5/18/16.
 */
public class DailyEventsReminderReceiver extends BroadcastReceiver {

    public static String ACTION = "com.evento.team2.eventspack.DailyEventsReminderReceiver";

    private DatabaseInteractor databaseInteractor;

    public DailyEventsReminderReceiver(DatabaseInteractor databaseInteractor) {
        this.databaseInteractor = databaseInteractor;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null
                && intent.getAction() != null
                && intent.getAction().equals(ACTION)) {

            ((EventiApplication) context.getApplicationContext()).getAppComponent().inject(this);

            new Thread() {
                @Override
                public void run() {
                    ArrayList<Event> savedEventsForToday = databaseInteractor.getSavedEventsInNext24Hours(String.valueOf(new Date().getTime()));

                    NotificationCompat.Builder savedEventNotification;

                    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                    Intent eventDetailsIntent;
                    PendingIntent resultPendingIntent;

                    for (Event event : savedEventsForToday) {
                        savedEventNotification =
                                new NotificationCompat.Builder(context)
                                        .setColor(context.getResources().getColor(R.color.colorPrimary))
                                        .setSmallIcon(R.drawable.eventi_notification_icon)
                                        .setContentTitle(event.name)
                                        .setContentText(DateFormatterUtils.fullDateFormat.format(new Date(event.startTimeStamp)))
                                        .setTicker(context.getString(R.string.saved_event_starts_today))
                                        .setCategory(Notification.CATEGORY_EVENT)
                                        .setAutoCancel(true)
                                        .setVibrate(new long[0])
                                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                                        .setLights(Color.GREEN, 1000, 1000);

                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        savedEventNotification.setSound(alarmSound);

                        eventDetailsIntent = ActivityEventDetails.createIntent(context, event.id);
                        eventDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                        resultPendingIntent = PendingIntent.getActivities(context, 0,
                                new Intent[]{eventDetailsIntent}, PendingIntent.FLAG_UPDATE_CURRENT);

                        savedEventNotification.setContentIntent(resultPendingIntent);

                        mNotificationManager.notify((int) event.id, savedEventNotification.build());
                    }
                }
            }.start();
        }
    }

    public static Intent getIntent() {
        return new Intent(ACTION);
    }
}
