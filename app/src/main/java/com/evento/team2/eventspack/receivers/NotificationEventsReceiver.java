package com.evento.team2.eventspack.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
 * Created by Daniel on 06-Mar-16.
 */
public class NotificationEventsReceiver extends BroadcastReceiver {

    public static String ACTION = "ActionNotificationEventsReceiver";

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
                            .setContentTitle(event.name)
                            .setContentText(DateFormatterUtils.fullDateFormat.format(new Date(event.startTimeStamp)));

            mBuilder.setCategory(Notification.CATEGORY_EVENT);
            mBuilder.setAutoCancel(true);
//
////            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
////            inboxStyle.setBigContentTitle("Event tracker details:");
////
////            String[] events = new String[6];
////            Arrays.fill(events, "test");
////            // Sets a title for the Inbox in expanded layout
////
////            // Moves events into the expanded layout
////            for (String event1 : events) {
////                inboxStyle.addLine(event1);
////            }
////
////            // Moves the expanded layout object into the notification object.
////            mBuilder.setStyle(inboxStyle);

            Intent eventDetailsIntent = ActivityEventDetails.createIntent(eventiApplication, event.id);
            eventDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent resultPendingIntent = PendingIntent.getActivities(eventiApplication, 0,
                    new Intent[]{eventDetailsIntent}, PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) eventiApplication.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify((int) event.id, mBuilder.build());
        }
    }

    public static Intent getIntent(long eventId) {
        Intent notificationEventIntent = new Intent(ACTION);
        notificationEventIntent.putExtra(ActivityEventDetails.EXTRA_ID, eventId);
        return notificationEventIntent;
    }
}
