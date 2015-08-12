package com.evento.team2.eventspack.adapter;

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
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.ui.activites.ActivityEventDetails;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 12-Aug-15.
 */
public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Event> events;
    private Context context;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        @Bind(R.id.event_picture)
        public ImageView mEventImage;
        @Bind(R.id.event_title)
        public TextView mEventTitle;
        @Bind(R.id.event_details)
        public TextView mEventDetails;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }

    public Event getValueAt(int position) {
        return events.get(position);
    }

    public EventsRecyclerViewAdapter(Context context, ArrayList<Event> eventNames) {
        this.context = context;
        this.events = eventNames;
    }

    public void addEvent(Event event) {
        events.add(0, event);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_events, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mEventTitle.setText(events.get(position).name);
        holder.mEventDetails.setText(events.get(position).details);

        if (TextUtils.isEmpty(events.get(position).pictureUri)) {
            Glide.with(holder.mEventImage.getContext()).load(R.drawable.party_image).fitCenter().into(holder.mEventImage);
        } else {
            // TODO daniel implement picture uri as picture
            Glide.with(context).load(new File(events.get(position).pictureUri)).fitCenter().into(holder.mEventImage);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                Intent intent = new Intent(context, ActivityEventDetails.class);
                intent.putExtra(ActivityEventDetails.EXTRA_NAME, events.get(position).name);
                if (!TextUtils.isEmpty(events.get(position).pictureUri)) {
                    intent.putExtra(ActivityEventDetails.EXTRA_PICTURE_URI, events.get(position).name);
                }

                context.startActivity(intent);
                // TODO daniel find better looking transition (do not forget to remove the library)
//                        ActivityTransitionLauncher.with((Activity) context).from(v).launch(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}