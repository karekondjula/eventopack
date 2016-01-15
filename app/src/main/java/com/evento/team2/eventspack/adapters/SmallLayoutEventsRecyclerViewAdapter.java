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
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.ui.activites.ActivityEventDetails;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 12-Aug-15.
 */
// TODO daniel NOT USED ... REMOVE IT!
public class SmallLayoutEventsRecyclerViewAdapter extends RecyclerView.Adapter<SmallLayoutEventsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Event> events;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        @Bind(R.id.small_event_picture)
        public ImageView mEventImage;
        @Bind(R.id.event_title)
        public TextView mEventTitle;
        @Bind(R.id.event_details)
        public TextView mEventDetails;
//        @Bind(R.id.btn_bookmark_icon)
//        public ToggleButton isEventSaved;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            // TODO maybe we should not use BK because we can not unbind the view
            ButterKnife.bind(this, view);
        }
    }

    public Event getValueAt(int position) {
        return events.get(position);
    }

    public SmallLayoutEventsRecyclerViewAdapter(Context context, ArrayList<Event> eventNames) {
        this.context = context;
        this.events = eventNames;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_small, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mEventTitle.setText(events.get(position).name);
        holder.mEventDetails.setText(events.get(position).details);

        if (TextUtils.isEmpty(events.get(position).pictureUri)) {
            Glide.with(holder.mEventImage.getContext()).load(R.drawable.party_image).into(holder.mEventImage);
        } else {
            Glide.with(context).load(events.get(position).pictureUri).into(holder.mEventImage);
        }

        holder.mView.setOnClickListener(v -> {

            Activity activity = (Activity) v.getContext();

            Intent intent = ActivityEventDetails.createIntent(context, events.get(position).id);
            activity.startActivity(intent);
            // TODO fancy animation please ^_^
//                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}