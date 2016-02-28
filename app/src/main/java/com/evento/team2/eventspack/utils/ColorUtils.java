package com.evento.team2.eventspack.utils;

import android.content.Context;

import com.evento.team2.eventspack.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Daniel on 22-Oct-15.
 */
public class ColorUtils {

    private ArrayList<Integer> colorsResources;
    private Random random = new Random();

    public HashMap<Long, Integer> dateColorHashMap;

    public ColorUtils() {
        colorsResources = new ArrayList<>();
        dateColorHashMap = new HashMap();

        try {
            for (Field field : R.color.class.getDeclaredFields()) {
                if (field.getName().startsWith("Color")) {
                    colorsResources.add(field.getInt(null));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public int getRandomColor(Context context) {
        return colorsResources.get(random.nextInt(colorsResources.size()));
    }
}
