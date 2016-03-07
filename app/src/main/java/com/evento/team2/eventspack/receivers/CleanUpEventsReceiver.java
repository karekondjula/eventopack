package com.evento.team2.eventspack.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;

import javax.inject.Inject;

/**
 * Created by Daniel on 02-Mar-16.
 */
public class CleanUpEventsReceiver extends BroadcastReceiver {

    public static String ACTION = "ActionCleanUpEventsReceiver";

    @Inject
    DatabaseInteractor databaseInteractor;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null
                && intent.getAction() != null
                && intent.getAction().equals(ACTION)) {
            ((EventiApplication) context.getApplicationContext()).getAppComponent().inject(this);

            new Thread() {
                @Override
                public void run() {
                    databaseInteractor.cleanUpEventsAndPlaces();

                    Intent intent = new Intent(context, DownloadEventsReceiver.class);
                    context.sendBroadcast(intent);
                }
            }.start();
        }
    }

    public static Intent getIntent() {
        return new Intent(ACTION);
    }
}
