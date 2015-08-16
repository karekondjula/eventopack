package com.evento.team2.eventspack.ui.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.utils.EventsController;
import com.evento.team2.eventspack.utils.Utils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import de.greenrobot.event.EventBus;

/**
 * Created by Daniel on 31-Jul-15.
 */
// TODO daniel butter-ify the code
public class FragmentMapWithEvents extends Fragment implements OnMapReadyCallback {

    private SupportMapFragment supportMapFragment;
    private GoogleMap mapView;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            // this might be buggy
            return view;
        }

        view = inflater.inflate(R.layout.fragment_map_with_events, container, false);

        FragmentManager fm = getChildFragmentManager();
        supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.location_map);
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.location_map, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (supportMapFragment != null) {
            if (getActivity() != null && supportMapFragment.isAdded()) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(supportMapFragment).commit();
            }
            supportMapFragment = null;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapView = googleMap;
        mapView.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapView.setMyLocationEnabled(true);
        mapView.getUiSettings().setCompassEnabled(true);
        mapView.getUiSettings().setRotateGesturesEnabled(true);

        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.party_image);
        Bitmap bhalfsize = Bitmap.createScaledBitmap(b, b.getWidth() / 4, b.getHeight() / 4, false);
        for (Event event : Utils.Helpers.createEvents()) {
            mapView.addMarker(new MarkerOptions()
                    .position(new LatLng(event.location.latitude,
                            event.location.longitude))
                    .title(event.name)
                    .icon(BitmapDescriptorFactory.fromBitmap(bhalfsize)));
        }

        mapView.getMyLocation();
//        SmartLocation.with(getActivity()).location()
//                .oneFix()
//                .start(new OnLocationUpdatedListener() {
//
//                    @Override
//                    public void onLocationUpdated(Location location) {
//                        mapView.getMyLocation()
//                    }
//                });

        // TODO daniel, go to my location, move FAB button if possible, span to events
    }

    public void onEvent(EventsController.UpdateEvents updateEvents) {
        // TODO refresh markers with new events
    }

    public static FragmentMapWithEvents newInstance(EventBus eventBus) {
        FragmentMapWithEvents f = new FragmentMapWithEvents();
        eventBus.register(f);
        return f;
    }
}