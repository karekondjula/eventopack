package com.evento.team2.eventspack.utils;

import android.content.Context;

import com.evento.team2.eventspack.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Daniel on 22-Oct-15.
 */
public class ColorUtils {

    private static ColorUtils instance;

    private ArrayList<Integer> colorsResources;
    private Random random = new Random();

    private ColorUtils() {
        colorsResources = new ArrayList<>();
        try {
            for (Field field : R.color.class.getDeclaredFields()) {
                colorsResources.add(field.getInt(null));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static ColorUtils getInstance() {
        if (instance == null) {
            instance = new ColorUtils();
        }

        return instance;
    }

    public int getRandomColor(Context context) {
        return colorsResources.get(random.nextInt(colorsResources.size()));
    }
}
