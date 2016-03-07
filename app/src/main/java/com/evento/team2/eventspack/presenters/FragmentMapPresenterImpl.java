package com.evento.team2.eventspack.presenters;

import android.content.Intent;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.models.Place;
import com.evento.team2.eventspack.presenters.interfaces.FragmentMapPresenter;
import com.evento.team2.eventspack.provider.FetchAsyncTask;
import com.evento.team2.eventspack.ui.activites.ActivityEventDetails;
import com.evento.team2.eventspack.ui.activites.ActivityPlaceDetails;
import com.evento.team2.eventspack.ui.fragments.FragmentMap;
import com.evento.team2.eventspack.utils.DateFormatterUtils;
import com.evento.team2.eventspack.utils.interfaces.MainThread;
import com.evento.team2.eventspack.views.FragmentMapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Daniel on 07-Mar-16.
 */
public class FragmentMapPresenterImpl implements FragmentMapPresenter {

    private EventiApplication eventiApplication;
    private DatabaseInteractor databaseInteractor;
    private MainThread mainThread;
    private FragmentMapView fragmentMapView;

    private HashMap<LatLng, Object> hashMapLatLngEventId = new HashMap<>();
    private Marker lastMarkerClicked;

    public FragmentMapPresenterImpl(EventiApplication eventiApplication, DatabaseInteractor databaseInteractor,
                                    MainThread mainThread) {
        this.eventiApplication = eventiApplication;
        this.databaseInteractor = databaseInteractor;
        this.mainThread = mainThread;
    }

    @Override
    public void setView(FragmentMapView fragmentMapView) {
        this.fragmentMapView = fragmentMapView;
    }

    @Override
    public void fetchEvents(String selectedDate) {
        fragmentMapView.showFetchingMarkers();
        new Thread() {
            @Override
            public void run() {
                ArrayList<Event> eventArrayList = databaseInteractor.getActiveEventsOnDate(selectedDate);

                ArrayList<MarkerOptions> markerOptionsArrayList = new ArrayList<>();
                MarkerOptions markerOptions;

                for (Event event : eventArrayList) {
                    if (event.location.latitude != 0 || event.location.longitude != 0) {
                        markerOptions = new MarkerOptions()
                                .position(new LatLng(event.location.latitude,
                                        event.location.longitude))
                                .title(event.name)
                                .snippet(event.details + FragmentMap.DELIMITER + DateFormatterUtils.fullDateFormat.format(event.startTimeStamp));

                        hashMapLatLngEventId.put(markerOptions.getPosition(), event);
                        markerOptionsArrayList.add(markerOptions);
                    }
                }

                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        fragmentMapView.showMarkers(markerOptionsArrayList);
                    }
                });
            }
        }.start();
    }

    @Override
    public void fetchPlaces() {
        fragmentMapView.showFetchingMarkers();
        new Thread() {
            @Override
            public void run() {
                ArrayList<Place> eventArrayList = databaseInteractor.getPlaces("");

                ArrayList<MarkerOptions> markerOptionsArrayList = new ArrayList<>();
                MarkerOptions markerOptions;

                for (Place place : eventArrayList) {
                    if (place.location.latitude != 0 || place.location.longitude != 0) {
                        markerOptions = new MarkerOptions()
                                .position(new LatLng(place.location.latitude,
                                        place.location.longitude))
                                .title(place.name);

                        hashMapLatLngEventId.put(markerOptions.getPosition(), place);
                        markerOptionsArrayList.add(markerOptions);
                    }
                }

                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        fragmentMapView.showMarkers(markerOptionsArrayList);
                    }
                });
            }
        }.start();
    }

    @Override
    public void fetchSavedEvents(long selectedDateTimeStamp) {
        fragmentMapView.showFetchingMarkers();
        new Thread() {
            @Override
            public void run() {
                ArrayList<Event> eventArrayList = databaseInteractor.getSavedEvents(
                        DateFormatterUtils.compareDateFormat.format(selectedDateTimeStamp));

                ArrayList<MarkerOptions> markerOptionsArrayList = new ArrayList<>();
                MarkerOptions markerOptions;

                for (Event event : eventArrayList) {
                    if (event.location.latitude != 0 || event.location.longitude != 0) {
                        markerOptions = new MarkerOptions()
                                .position(new LatLng(event.location.latitude,
                                        event.location.longitude))
                                .title(event.name)
                                .snippet(event.details + FragmentMap.DELIMITER +
                                        DateFormatterUtils.fullDateFormat.format(event.startTimeStamp));

                        hashMapLatLngEventId.put(markerOptions.getPosition(), event);
                        markerOptionsArrayList.add(markerOptions);
                    }
                }

                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        fragmentMapView.showMarkers(markerOptionsArrayList);
                    }
                });
            }
        }.start();
    }

    @Override
    public void goToEvent(long eventId) {
        Event event = databaseInteractor.getEventById(eventId);
        fragmentMapView.goToLocation(event.location.latitude, event.location.longitude);
    }

    @Override
    public void goToPlace(long placeId) {
        Place place = databaseInteractor.getPlaceById(placeId);
        fragmentMapView.goToLocation(place.location.latitude, place.location.longitude);
    }

    @Override
    public void markerClicked(Marker marker, int selectedMode) {

        if (marker == null) {
            lastMarkerClicked = null;
            return;
        }

        if (lastMarkerClicked != null && lastMarkerClicked.equals(marker)) {
            Intent intent = null;
            if (selectedMode == FetchAsyncTask.EVENTS || selectedMode == FetchAsyncTask.SAVED_EVENTS) {
                intent = ActivityEventDetails.createIntent(eventiApplication, ((Event) hashMapLatLngEventId.get(marker.getPosition())).id);
            } else if (selectedMode == FetchAsyncTask.PLACES) {
                intent = ActivityPlaceDetails.createIntent(eventiApplication, ((Place) hashMapLatLngEventId.get(marker.getPosition())).id);
            }
            eventiApplication.startActivity(intent);
        } else {
            lastMarkerClicked = marker;

            if (selectedMode == FetchAsyncTask.EVENTS || selectedMode == FetchAsyncTask.SAVED_EVENTS) {

                Event event = (Event) hashMapLatLngEventId.get(marker.getPosition());
                fragmentMapView.showEventSelected(event);
            } else if (selectedMode == FetchAsyncTask.PLACES) {

                Place place = (Place) hashMapLatLngEventId.get(marker.getPosition());
                fragmentMapView.showPlaceSelected(place);
            }
        }
    }
}
