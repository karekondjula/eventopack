package com.evento.team2.eventspack.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.model.Place;
import com.evento.team2.eventspack.ui.activites.ActivityPlaceDetails;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 12-Aug-15.
 */
public class PlacesRecyclerViewAdapter extends RecyclerView.Adapter<PlacesRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Place> places;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        @Bind(R.id.place_picture)
        public ImageView mPlacePicture;
        @Bind(R.id.place_title)
        public TextView mPlaceTitle;
        @Bind(R.id.place_location)
        public TextView mPlaceLocation;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            // TODO maybe we should not use BK because we can not unbind the view
            // also in Events adapter
            ButterKnife.bind(this, view);
        }
    }

    public void addPlaces(ArrayList<Place> placesArrayList) {
        if (!places.equals(placesArrayList)) {
            places.clear();
            places.addAll(placesArrayList);
        }
    }

    public PlacesRecyclerViewAdapter(Context context) {
        this.context = context;
        places = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Place place = places.get(position);

        holder.mPlaceTitle.setText(place.name);

        if (TextUtils.isEmpty(place.pictureUri)) {
            Glide.with(holder.mPlacePicture.getContext()).load(R.drawable.place_image).into(holder.mPlacePicture);
        } else {
            // TODO daniel implement picture uri as picture
            Glide.with(context).load(new File(place.pictureUri)).into(holder.mPlacePicture);
        }

        holder.mPlaceLocation.setText(place.locationString);

        holder.mView.setOnClickListener(v -> {

            Activity activity = (Activity) v.getContext();

            Intent intent = ActivityPlaceDetails.createIntent(context, place.id);
            activity.startActivity(intent);

            // TODO fancy animation please ^_^
//                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }
}