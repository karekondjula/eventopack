package com.evento.team2.eventspack.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.interactors.interfaces.PreferencesInteractor;
import com.evento.team2.eventspack.soapservice.ServiceEventoImpl;
import com.evento.team2.eventspack.soapservice.interfaces.ServiceEvento;

import java.util.Date;
import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by Daniel on 02-Mar-16.
 */
public class DownloadEventsReceiver extends BroadcastReceiver {

    public static String ACTION = "ActionDownloadEventsReceiver";

    @Inject
    PreferencesInteractor preferencesInteractor;

    @Inject
    ServiceEvento serviceEvento;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null
                && intent.getAction() != null
                && intent.getAction().equals(ACTION)) {

            ((EventiApplication) context.getApplicationContext()).getAppComponent().inject(this);

            new Thread() {
                @Override
                public void run() {
                    serviceEvento.getAllCurrentEvents();
                    preferencesInteractor.setLastUpdateOfEvents(new Date().getTime());
                }
            }.start();
        }
    }

    public static Intent getIntent() {
        return new Intent(ACTION);
    }
}
