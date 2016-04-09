package com.evento.team2.eventspack.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.interactors.interfaces.NotificationsInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.ui.activites.ActivityEventDetails;
import com.evento.team2.eventspack.utils.DateFormatterUtils;
import com.joanzapata.iconify.widget.IconTextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 12-Aug-15.
 */
public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.ViewHolder> {

    private static final String ICON_TEXT_VIEW_FILLED_HEART = "{ion-android-favorite @color/colorPrimary}";
    private static final String ICON_TEXT_VIEW_EMPTY_HEART = "{ion-android-favorite-outline @color/colorPrimary}";
    private final static Date TODAY = new Date();

    private Context context;
    private ArrayList<Event> events;
    private Calendar calendar;

    private NotificationsInteractor notificationsInteractor;
    private DatabaseInteractor databaseInteractor;

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
        @Bind(R.id.eventAttending)
        View mEventAttending;
        @Bind(R.id.eventAttendingCount)
        TextView mEventAttendingCount;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            // TODO maybe we should not use BK because we can not unbind the view
            ButterKnife.bind(this, view);
        }
    }

    public EventsRecyclerViewAdapter(Context context, NotificationsInteractor notificationsInteractor, DatabaseInteractor databaseInteractor) {
        this.context = context;
        this.notificationsInteractor = notificationsInteractor;
        this.databaseInteractor = databaseInteractor;

        events = new ArrayList<>();

        calendar = Calendar.getInstance();
        calendar.setTime(TODAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
    }

    public void addEvents(ArrayList<Event> eventArrayList) {
        // TODO add fancy logic, to find the exact place an event should fit
        // so we use the automatic animation by android (hash maps<id, position>?)
        if (!events.equals(eventArrayList)) {
            events.clear();
            notifyDataSetChanged();

            for (Event event : eventArrayList) {
                events.add(event);
                notifyItemInserted(events.size());
            }
        }
    }

//    public void addEvents(ArrayList<Event> eventArrayList) {
//        if (!events.equals(eventArrayList)) {
//            events.clear();
//            events.addAll(eventArrayList);
//        }
//// else {
////            Log.i(">>", "same ol' events!!!");
////        }
//    }

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

        try {
            Date startDateEvent, endDateEvent;
            startDateEvent = DateFormatterUtils.compareDateFormat.parse(event.startDateString);

            if (Math.abs(startDateEvent.getTime() - calendar.getTimeInMillis()) < 1000) {
                // today (or close enough)
                holder.mEventStartTime.setText(event.startTimeStamp != 0 ? context.getString(R.string.today)
                        + " " + DateFormatterUtils.hoursMinutesDateFormat.format(event.startTimeStamp) : "");
                holder.mEventStartTime.setTextColor(context.getResources().getColor(R.color.colorAccent));
//            holder.mEventEndTime.setTextColor(context.getResources().getColor(R.color.colorAccent));
            } else if (startDateEvent.getTime() - calendar.getTimeInMillis() < 0) {
                // event has expired
                holder.mEventStartTime.setText(event.startTimeStamp != 0 ? DateFormatterUtils.fullDateFormat.format(event.startTimeStamp)
                        : "");
                holder.mEventStartTime.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            } else {
                // event is on a day != from today
                holder.mEventStartTime.setText(event.startTimeStamp != 0 ? DateFormatterUtils.fullDateFormat.format(event.startTimeStamp)
                        : "");
                holder.mEventStartTime.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
//            holder.mEventEndTime.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            }

            // TODO refactor
            if (event.endTimeStamp != 0) {
                endDateEvent = DateFormatterUtils.compareDateFormat.parse(event.endDateString);
                if (event.endTimeStamp - calendar.getTimeInMillis() < 0) {
                    // end time has also expired
                    holder.mEventEndTime.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                } else {
                    if (Math.abs(endDateEvent.getTime() - calendar.getTimeInMillis()) < 1000) {
                        // end time is today
                        holder.mEventEndTime.setText(context.getString(R.string.today) + " " + DateFormatterUtils.hoursMinutesDateFormat.format(event.endTimeStamp));
                        holder.mEventEndTime.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    } else {
                        // end time is still active
                        holder.mEventEndTime.setText(DateFormatterUtils.fullDateFormat.format(event.endTimeStamp));
                        holder.mEventEndTime.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                    }
                }
            } else {
                holder.mEventEndTime.setText("");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(event.pictureUri)) {
            Glide.with(holder.mEventImage.getContext()).load(R.drawable.party_image).into(holder.mEventImage);
        } else {
            Glide.with(context).load(event.pictureUri).into(holder.mEventImage);
        }

        holder.mEventLocation.setText(event.locationString);

        if (event.isEventSaved) {
            holder.isEventSaved.setText(ICON_TEXT_VIEW_FILLED_HEART);
        } else {
            holder.isEventSaved.setText(ICON_TEXT_VIEW_EMPTY_HEART);
        }

        if (!TextUtils.isEmpty(event.attendingCount)) {
            holder.mEventAttendingCount.setText(event.attendingCount);
        } else {
            holder.mEventAttending.setVisibility(View.GONE);
        }

        holder.isEventSaved.setOnClickListener(v -> {

            event.isEventSaved = !event.isEventSaved;

            if (event.isEventSaved) {
                holder.isEventSaved.setText(ICON_TEXT_VIEW_FILLED_HEART);
                notificationsInteractor.scheduleNotification(event);
            } else {
                holder.isEventSaved.setText(ICON_TEXT_VIEW_EMPTY_HEART);
                notificationsInteractor.removeScheduleNotification(event);
            }
            databaseInteractor.changeSaveEvent(event, event.isEventSaved);

            try {
                Snackbar.make(v,
                        event.isEventSaved ?
                                String.format(context.getResources().getString(R.string.event_is_saved, event.name)) :
                                String.format(context.getResources().getString(R.string.event_is_removed, event.name)),
                        Snackbar.LENGTH_LONG)
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }

//                YoYo.with(Techniques.Tada)
//                        .duration(700)
//                        .playOn(v);
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity activity = (Activity) v.getContext();

                Intent intent = ActivityEventDetails.createIntent(context, event.id);
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

    @Override
    public long getItemId(int position) {
        return events.get(position).id;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }
}