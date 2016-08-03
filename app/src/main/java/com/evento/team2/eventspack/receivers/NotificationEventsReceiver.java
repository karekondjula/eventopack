package com.evento.team2.eventspack.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.ui.activites.ActivityEventDetails;
import com.evento.team2.eventspack.utils.DateFormatterUtils;

import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by Daniel on 06-Mar-16.
 */
public class NotificationEventsReceiver extends BroadcastReceiver {

    public static String ACTION = "ActionNotificationEventsReceiver";

    private DatabaseInteractor databaseInteractor;

    public NotificationEventsReceiver(DatabaseInteractor databaseInteractor) {
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
                    Bundle bundle = intent.getExtras();

                    Event event = databaseInteractor.getEventById(bundle.getLong(ActivityEventDetails.EXTRA_EVENT_ID));

                    NotificationCompat.Builder savedEventNotification =
                            new NotificationCompat.Builder(context)
                                    .setColor(context.getResources().getColor(R.color.colorPrimary))
                                    .setSmallIcon(R.drawable.eventi_notification_icon)
                                    .setContentTitle(event.name)
                                    .setContentText(DateFormatterUtils.fullDateFormat.format(new Date(event.startTimeStamp)))
                                    .setTicker(context.getString(R.string.upcoming_event_reminder))
                                    .setCategory(Notification.CATEGORY_EVENT)
                                    .setAutoCancel(true)
                                    .setVibrate(new long[0])
                                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                                    .setLights(Color.GREEN, 1000, 1000);

                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    savedEventNotification.setSound(alarmSound);

                    Bitmap b = null;
                    try {
                        if (!TextUtils.isEmpty(event.pictureUri)) {
                            b = Glide.with(context).load(event.pictureUri).asBitmap().into(-1, -1).get();
                        } else {
                            b = Glide.with(context).load(R.drawable.party_image).asBitmap().into(-1, -1).get();
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }

                    NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
                    bigPictureStyle.bigPicture(b);
                    bigPictureStyle.setSummaryText(DateFormatterUtils.fullDateFormat.format(new Date(event.startTimeStamp)));

                    savedEventNotification.setStyle(bigPictureStyle);

                    Intent eventDetailsIntent = ActivityEventDetails.createIntent(context, event.id);
                    eventDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    PendingIntent resultPendingIntent = PendingIntent.getActivities(context, 0, new Intent[]{eventDetailsIntent}, PendingIntent.FLAG_UPDATE_CURRENT);

                    savedEventNotification.setContentIntent(resultPendingIntent);

                    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                    mNotificationManager.notify((int) event.id, savedEventNotification.build());
                }
            }.start();
        }
    }

    public static Intent getIntent(long eventId) {
        Intent notificationEventIntent = new Intent(ACTION);
        notificationEventIntent.putExtra(ActivityEventDetails.EXTRA_EVENT_ID, eventId);
        return notificationEventIntent;
    }
}
