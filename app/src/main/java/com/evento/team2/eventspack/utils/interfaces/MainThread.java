package com.evento.team2.eventspack.utils.interfaces;

/**
 * Created by Daniel on 15-Jan-16.
 */
public interface MainThread {
    void post(Runnable runnable);

    void postDelayed(Runnable runnable, long delayInMs);

    void removeCallbacks();
}
