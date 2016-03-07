package com.evento.team2.eventspack.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Daniel on 14-Oct-15.
 */
public class NetworkUtils {

    private ConnectivityManager connectivityManager;

    public NetworkUtils(Context context) {
        connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
