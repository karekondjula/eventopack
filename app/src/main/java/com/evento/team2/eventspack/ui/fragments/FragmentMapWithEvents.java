package com.evento.team2.eventspack.ui.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.utils.Utils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.Bind;

/**
 * Created by Daniel on 31-Jul-15.
 */
// TODO daniel butter-ify the code
public class FragmentMapWithEvents extends Fragment implements OnMapReadyCallback {

    private SupportMapFragment map;
    private GoogleMap mapView;

    public FragmentMapWithEvents() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_with_events, container, false);

        FragmentManager fm = getChildFragmentManager();
        map = (SupportMapFragment) fm.findFragmentById(R.id.location_map);
        if (map == null) {
            map = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.location_map, map).commit();
        }
        map.getMapAsync(this);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (map != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.location_map)).commit();
            map = null;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapView = googleMap;
        mapView.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapView.getUiSettings().setMyLocationButtonEnabled(true);
        mapView.getUiSettings().setCompassEnabled(true);
        mapView.getUiSettings().setRotateGesturesEnabled(true);

        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.party_image);
        Bitmap bhalfsize = Bitmap.createScaledBitmap(b, b.getWidth() / 4, b.getHeight() / 4, false);
        for (Event event : Utils.Helpers.createEvents()) {
            mapView.addMarker(new MarkerOptions()
                    .position(new LatLng(event.position.latitude,
                            event.position.longitude))
                    .title(event.name)
                    .icon(BitmapDescriptorFactory.fromBitmap(bhalfsize)));
        }

    }

    public static FragmentMapWithEvents newInstance() {
        FragmentMapWithEvents f = new FragmentMapWithEvents();
        return f;
    }
}