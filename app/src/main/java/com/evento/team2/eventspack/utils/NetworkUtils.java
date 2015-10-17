package com.evento.team2.eventspack.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Daniel on 14-Oct-15.
 */
public class NetworkUtils {

    private static volatile NetworkUtils instance;

    private NetworkUtils() {
    }

    public static NetworkUtils getInstance() {
        if (instance == null) {
            instance = new NetworkUtils();
        }

        return instance;
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
