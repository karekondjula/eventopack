package com.evento.team2.eventspack.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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

    static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        @Bind(R.id.event_picture)
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
            ButterKnife.bind(this, view);
        }
    }

    public Event getValueAt(int position) {
        return events.get(position);
    }

    public EventsRecyclerViewAdapter(Context context) {
        this.context = context;
        events = new ArrayList<>();
    }

    public void addEvent(Event event) {
        events.add(0, event);
    }

    public void addEvents(ArrayList<Event> eventArrayList) {
        if (!events.equals(eventArrayList)) {
            Log.i(">>", "time to clear list, new events!!!");
            events.clear();
            events.addAll(eventArrayList);
        } else {
            Log.i(">>", "same ol' events!!!");
        }
    }

    public ArrayList<Event> getEventsList() {
        return events;
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
            Glide.with(holder.mEventImage.getContext()).load(R.drawable.party_image).into(holder.mEventImage);
        } else {
            // TODO daniel implement picture uri as picture
            Glide.with(context).load(new File(events.get(position).pictureUri)).into(holder.mEventImage);
        }

//        holder.isEventSaved.setChecked(events.get(position).isEventSaved);
//
//        holder.isEventSaved.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                events.get(position).isEventSaved = !events.get(position).isEventSaved;
//
//                if (events.get(position).isEventSaved) {
//                    EventsDatabase.getInstance().saveEvent(events.get(position));
//                } else {
//                    EventsDatabase.getInstance().removeSavedEvent(events.get(position));
//                }
//                EventsController.getInstance().getEventBus().post(new EventsController.UpdateSavedEvents());
//
//                Snackbar.make(v,
//                            events.get(position).isEventSaved ?
//                                    "JsonEvent is saved" :
//                                    "JsonEvent is removed from saved events",
//                            Snackbar.LENGTH_LONG)
//                        .setAction("Undo", null)
//                        .setActionTextColor(Color.RED)
//                        .show();
//
//                YoYo.with(Techniques.Tada)
//                        .duration(700)
//                        .playOn(v);
//            }
//        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity activity = (Activity) v.getContext();

                Intent intent = new Intent(context, ActivityEventDetails.class);
                intent.putExtra(ActivityEventDetails.EXTRA_NAME, events.get(position).name);
                // TODO put whole event as extra, easier to do a save
                if (!TextUtils.isEmpty(events.get(position).pictureUri)) {
                    intent.putExtra(ActivityEventDetails.EXTRA_PICTURE_URI, events.get(position).name);
                }

                activity.startActivity(intent);
                // TODO fancy animation please ^_^
//                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}