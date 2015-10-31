package com.evento.team2.eventspack.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.Snackbar;
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
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.evento.team2.eventspack.ui.activites.ActivityEventDetails;
import com.evento.team2.eventspack.ui.fragments.FragmentMap;
import com.joanzapata.iconify.widget.IconTextView;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 12-Aug-15.
 */
public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.ViewHolder> {

    private static final String ICON_TEXT_VIEW_FILLED_HEART = "{ion-android-favorite @color/colorPrimary}";
    private static final String ICON_TEXT_VIEW_EMPTY_HEART = "{ion-android-favorite-outline @color/colorPrimary}";

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
        @Bind(R.id.event_start_time)
        public TextView mEventStartTime;
        @Bind(R.id.event_end_time)
        public TextView mEventEndTime;
        @Bind(R.id.event_location)
        public TextView mEventLocation;
        @Bind(R.id.btn_save_icon)
        public IconTextView isEventSaved;

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
//            Log.i(">>", "time to clear list, new events!!!");
            events.clear();
            events.addAll(eventArrayList);
        } else {
//            Log.i(">>", "same ol' events!!!");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Event event = events.get(position);

        holder.mEventTitle.setText(event.name);
        holder.mEventDetails.setText(event.details);

        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
        String startEndTimes[] = String.format(context.getString(R.string.event_details_time),
                dateFormat.format(event.startTimeStamp),
                dateFormat.format(event.endTimeStamp)).split(FragmentMap.DELIMITER);
        holder.mEventStartTime.setText(startEndTimes[0]);
        holder.mEventEndTime.setText(startEndTimes[1]);

        if (TextUtils.isEmpty(event.pictureUri)) {
            Glide.with(holder.mEventImage.getContext()).load(R.drawable.party_image).into(holder.mEventImage);
        } else {
            // TODO daniel implement picture uri as picture
            Glide.with(context).load(new File(event.pictureUri)).into(holder.mEventImage);
        }

        holder.mEventLocation.setText(event.locationString);

        if (event.isEventSaved) {
            holder.isEventSaved.setText(ICON_TEXT_VIEW_FILLED_HEART);
        } else {
            holder.isEventSaved.setText(ICON_TEXT_VIEW_EMPTY_HEART);
        }

        holder.isEventSaved.setOnClickListener(v -> {

            event.isEventSaved = !event.isEventSaved;

            if (event.isEventSaved) {
                holder.isEventSaved.setText(ICON_TEXT_VIEW_FILLED_HEART);
            } else {
                holder.isEventSaved.setText(ICON_TEXT_VIEW_EMPTY_HEART);
            }
            EventsDatabase.getInstance().changeSaveEvent(event);

            Snackbar.make(v,
                    event.isEventSaved ?
                            String.format(context.getResources().getString(R.string.event_is_saved, event.name)) :
                            String.format(context.getResources().getString(R.string.event_is_removed, event.name)),
                    Snackbar.LENGTH_LONG)
                    .show();

//                YoYo.with(Techniques.Tada)
//                        .duration(700)
//                        .playOn(v);
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity activity = (Activity) v.getContext();

                Intent intent = new Intent(context, ActivityEventDetails.class);
                intent.putExtra(ActivityEventDetails.EXTRA_ID, event.id);

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