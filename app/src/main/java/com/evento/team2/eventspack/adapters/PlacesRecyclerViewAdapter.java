package com.evento.team2.eventspack.adapters;

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
import com.evento.team2.eventspack.models.Place;
import com.evento.team2.eventspack.ui.activites.ActivityPlaceDetails;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 12-Aug-15.
 */
public class PlacesRecyclerViewAdapter extends RecyclerView.Adapter<PlacesRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Place> places;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        @BindView(R.id.place_picture)
        public ImageView mPlacePicture;
        @BindView(R.id.place_title)
        public TextView mPlaceTitle;
//        @BindView(R.id.place_location) TODO maybe number of events in the place?!
//        public TextView mPlaceLocation;

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
            notifyDataSetChanged();
//            places.addAll(placesArrayList);

            for (Place place : placesArrayList) {
                places.add(place);
                notifyItemInserted(places.size());
            }
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
            Glide.with(context).load(place.pictureUri).into(holder.mPlacePicture);
        }

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

    @Override
    public long getItemId(int position) {
        return places.get(position).id;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }
}