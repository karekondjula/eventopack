package com.evento.team2.eventspack.interactors.interfaces;

import android.content.Intent;

/**
 * Created by Daniel on 06-Mar-16.
 */
public interface AlarmManagerInteractor {

    void scheduleRepeating(Intent intent, long startAt, long repeatIn);

    void scheduleOneTime(Intent intent, long startAt);

    void removeScheduledEvent(Intent intent);
}
