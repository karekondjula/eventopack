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
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.ui.activites.ActivityEventDetails;
import com.evento.team2.eventspack.ui.activites.ActivityMain;
import com.evento.team2.eventspack.utils.DateFormatterUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by daniel-kareski on 5/18/16.
 */
public class DailyEventsReminderReceiver extends BroadcastReceiver {

    private final static String GROUP_DAILY_EVENTS = "group_daily_events";

    public static String ACTION = "ActionDailyEventsReminderReceiver";

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

                    if (savedEventsForToday.size() > 0) {
                        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                        NotificationCompat.Builder savedEventNotification =
                                new NotificationCompat.Builder(context)
                                        .setColor(context.getResources().getColor(R.color.colorPrimary))
                                        .setSmallIcon(R.drawable.eventi_notification_icon)
                                        .setTicker(context.getString(R.string.upcoming_event_reminder))
                                        .setCategory(Notification.CATEGORY_EVENT)
                                        .setAutoCancel(true)
                                        .setVibrate(new long[0])
                                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                                        .setLights(Color.GREEN, 1000, 1000)
                                        .setSound(alarmSound);

                        if (savedEventsForToday.size() > 1) {
                            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                            savedEventNotification.setStyle(inboxStyle);

                            for (Event event : savedEventsForToday) {
                                inboxStyle.addLine(event.name);
                            }

                            String title = context.getResources().getQuantityString(R.plurals.number_of_saved_events, savedEventsForToday.size(), savedEventsForToday.size());
                            inboxStyle.setBigContentTitle(title);
                            savedEventNotification.setContentTitle(title);

                            Intent savedEventsIntent = ActivityMain.createIntent(context);
                            savedEventsIntent.setAction(ActivityMain.ACTION_SAVED_EVENTS);
                            PendingIntent resultPendingIntent = PendingIntent.getActivities(context, 0, new Intent[]{savedEventsIntent}, PendingIntent.FLAG_UPDATE_CURRENT);

                            savedEventNotification.setContentIntent(resultPendingIntent);

                            mNotificationManager.notify(1, savedEventNotification.build());

                        } else if (savedEventsForToday.size() == 1) {

                            Event event = savedEventsForToday.get(0);

                            savedEventNotification.setContentTitle(event.name);
                            savedEventNotification.setContentText(DateFormatterUtils.fullDateFormat.format(new Date(event.startTimeStamp)));

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
                            eventDetailsIntent.setAction(String.valueOf(event.id));
                            eventDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                            PendingIntent resultPendingIntent = PendingIntent.getActivities(context, 0, new Intent[]{eventDetailsIntent}, PendingIntent.FLAG_ONE_SHOT);

                            savedEventNotification.setContentIntent(resultPendingIntent);

                            mNotificationManager.notify((int) event.id, savedEventNotification.build());
                        }
                    }
                }
            }.start();
        }
    }

    public static Intent getIntent() {
        return new Intent(ACTION);
    }
}
