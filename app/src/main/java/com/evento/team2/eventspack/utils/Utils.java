package com.evento.team2.eventspack.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.evento.team2.eventspack.models.Event;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Created by Daniel on 04-Aug-15.
 */
public class Utils {
    public final static class Helpers {

        static final String[] EVENTS = {"Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",};
        static final String[] EVENTS_DESCRIPTION = {"auch jakosadf sdfsdfasdfasdfsadfsad", "majkata na ", "albert", "donzuav", "japanac",};

        public static ArrayList<Event> events;

        public static synchronized ArrayList<Event> createEvents() {

            if (events == null) {
                events = new ArrayList<>();

                int i = 0;

                Event dummyEvent;

                for (String eventNames : EVENTS) {

                    dummyEvent = new Event(eventNames, EVENTS_DESCRIPTION[i++]);
                    dummyEvent.id = i;

                    dummyEvent.location = new LatLng(42.0016727 + (new Random().nextDouble() / (new Random().nextBoolean() ? 1000 : -100)),
                            21.4085439 + (new Random().nextDouble() / (new Random().nextBoolean() ? 100 : -1000)));

                    dummyEvent.startTimeStamp = System.currentTimeMillis()
                            + (new Random().nextBoolean() ? 13579l * new Random().nextInt(97531) : 0);

                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(dummyEvent.startTimeStamp);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);

                    dummyEvent.startDate = cal.getTimeInMillis();

                    String dateTime = new SimpleDateFormat("HH:mm dd.MM.yyyy").format(dummyEvent.startTimeStamp);

                    dummyEvent.startTimeString = dateTime.split(" ")[0];
                    dummyEvent.startDateString = dateTime.split(" ")[1];
                    events.add(dummyEvent);
                }
            }
            return events;
        }

        public static String getEventsJson() {
            return "";
        }
    }

    public static void openImageInGallery(Context context, String pictureUri) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Bitmap bmp = Glide.with(context).load(pictureUri).asBitmap().into(-1, -1).get();

                    FileOutputStream out = null;
                    try {
                        File tempFile = File.createTempFile("temp", ".jpg");
                        out = new FileOutputStream(tempFile);

                        bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                        out.flush();
                        out.close();
                        tempFile.setReadable(true, false);

                        MimeTypeResolver.startActivityWithMimeType(context, tempFile.getAbsoluteFile().getAbsolutePath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (InterruptedException | ExecutionException ie) {
                    ie.printStackTrace();
                }
            }
        }.start();
    }
}
