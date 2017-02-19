package com.evento.team2.eventspack.utils;

import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by d-kareski on 2/19/17.
 */

public class AdsUtils {

    private static boolean USE_ADS = true;

    public static void enableAds(AdView adView) {
        if (USE_ADS) {
            AdRequest adRequest = new AdRequest.Builder()
                    .setRequestAgent("eventi:banner_ad")
                    .build();
            adView.loadAd(adRequest);
        } else {
            adView.setVisibility(View.GONE);
        }
    }
}