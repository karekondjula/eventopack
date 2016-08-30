package com.evento.team2.eventspack.interactors;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.interactors.interfaces.AlarmManagerInteractor;
import com.evento.team2.eventspack.ui.activites.ActivityEventDetails;

/**
 * Created by Daniel on 06-Mar-16.
 */
public class AlarmManagerInteractorImpl implements AlarmManagerInteractor{

    private EventiApplication eventiApplication;
    private AlarmManager alarmManager;

    public AlarmManagerInteractorImpl(EventiApplication eventiApplication) {
        this.eventiApplication = eventiApplication;
        alarmManager = (AlarmManager) eventiApplication.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public void scheduleRepeating(Intent intent, long startAt, long repeatIn) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(eventiApplication, 0, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC, startAt, repeatIn, pendingIntent);
    }

    @Override
    public void scheduleOneTime(Intent intent, long startAt) {
        int id = (int) intent.getExtras().getLong(ActivityEventDetails.EXTRA_EVENT_ID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(eventiApplication, id, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, startAt, pendingIntent);
    }

    @Override
    public void removeScheduledEvent(Intent intent) {
        int id = (int) intent.getExtras().getLong(ActivityEventDetails.EXTRA_EVENT_ID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(eventiApplication, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
    }

}
