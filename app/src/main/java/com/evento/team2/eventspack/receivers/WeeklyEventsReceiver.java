package com.evento.team2.eventspack.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.ui.activites.ActivityEventDetails;
import com.evento.team2.eventspack.utils.DateFormatterUtils;

import java.util.Date;

import javax.inject.Inject;

/**
 * Created by Daniel on 08-Mar-16.
 */
public class WeeklyEventsReceiver extends BroadcastReceiver {

    public static String ACTION = "WeeklyEventsReceiver";

    @Inject
    EventiApplication eventiApplication;

    @Inject
    DatabaseInteractor databaseInteractor;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null
                && intent.getAction() != null
                && intent.getAction().equals(ACTION)) {

            ((EventiApplication) context.getApplicationContext()).getAppComponent().inject(this);

            Bundle bundle = intent.getExtras();

            Event event = databaseInteractor.getEventById(bundle.getLong(ActivityEventDetails.EXTRA_ID));

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(eventiApplication)
                            .setColor(eventiApplication.getResources().getColor(R.color.colorPrimary))
                            .setSmallIcon(R.drawable.eventi_notification_icon)
                            .setContentTitle("You have Saved events for this week")
                            .setTicker("Saved events weekly reminder")
                            .setCategory(Notification.CATEGORY_EVENT)
                            .setAutoCancel(true)
                            .setVisibility(Notification.VISIBILITY_PUBLIC)
                            .setVibrate(new long[0]);

//            Intent eventDetailsIntent = ActivityEventDetails.createIntent(eventiApplication, event.id);
//            eventDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            PendingIntent resultPendingIntent = PendingIntent.getActivities(eventiApplication, 0,
//                    new Intent[]{eventDetailsIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
//            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) eventiApplication.getSystemService(Context.NOTIFICATION_SERVICE);

            Notification notification = mBuilder.build();
            notification.ledOnMS = 1000;
            notification.ledOffMS = 1000;
            notification.flags |= Notification.FLAG_SHOW_LIGHTS;
            notification.ledARGB = Color.WHITE;

            mNotificationManager.notify((int) event.id, notification);
        }
    }

    public static Intent getIntent() {
        return new Intent(ACTION);
    }
}
