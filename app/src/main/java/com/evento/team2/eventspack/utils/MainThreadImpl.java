package com.evento.team2.eventspack.utils;

import android.os.Handler;
import android.os.Looper;

import com.evento.team2.eventspack.utils.interfaces.MainThread;

/**
 * Created by Daniel on 15-Jan-16.
 */
public class MainThreadImpl implements MainThread {
    private Handler handler;

    public MainThreadImpl() {
        this.handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable) {
        // create new handler since removeCallbacks will de-register the last (in the singleton) to prevent memory leak
        if (handler == null) this.handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }

    @Override
    public void postDelayed(Runnable runnable, long delayInMs) {
        // create new handler since removeCallbacks will de-register the last (in the singleton) to prevent memory leak
        if (handler == null) this.handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(runnable, delayInMs);
    }

    @Override
    public void removeCallbacks() {
        if (handler == null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }
}
