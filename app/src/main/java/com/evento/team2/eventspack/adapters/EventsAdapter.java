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
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.presenters.interfaces.FragmentEventsPresenter;
import com.evento.team2.eventspack.ui.activites.ActivityEventDetails;
import com.evento.team2.eventspack.utils.DateFormatterUtils;
import com.joanzapata.iconify.widget.IconTextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 12-Aug-15.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private static final String ICON_TEXT_VIEW_FILLED_HEART = "{ion-android-favorite @color/colorPrimary}";
    private static final String ICON_TEXT_VIEW_EMPTY_HEART = "{ion-android-favorite-outline @color/colorPrimary}";
    private final static Date TODAY = new Date();

    private Context context;
    private ArrayList<Event> events;
    private Calendar calendar;

    private FragmentEventsPresenter presenter;

    static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        @BindView(R.id.event_picture)
        public ImageView mEventImage;
        @BindView(R.id.event_title)
        public TextView mEventTitle;
        @BindView(R.id.event_details)
        public TextView mEventDetails;
        @BindView(R.id.event_start_time)
        public TextView mEventStartTime;
        @BindView(R.id.event_end_time)
        public TextView mEventEndTime;
        @BindView(R.id.event_location)
        public TextView mEventLocation;
        @BindView(R.id.btn_save_icon)
        public IconTextView isEventSaved;
        @BindView(R.id.eventAttending)
        View mEventAttending;
        @BindView(R.id.eventAttendingCount)
        TextView mEventAttendingCount;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            // TODO maybe we should not use BK because we can not unbind the view
            ButterKnife.bind(this, view);
        }
    }

    public EventsAdapter(Context context, FragmentEventsPresenter presenter) {
        this.context = context;
        this.presenter = presenter;

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

            if (!event.isEventSaved) {
                holder.isEventSaved.setText(ICON_TEXT_VIEW_FILLED_HEART);
            } else {
                holder.isEventSaved.setText(ICON_TEXT_VIEW_EMPTY_HEART);
            }

            presenter.changeSavedStateOfEvent(event);

            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .playOn(v);
        });

        holder.mView.setOnClickListener(view -> {

            Activity activity = (Activity) view.getContext();

            Intent intent = ActivityEventDetails.createIntent(context, event.id);

//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                ActivityOptions options = ActivityOptions
//                        .makeSceneTransitionAnimation(activity, holder.mEventImage, EventiConstants.TRANSITION_EVENT_IMAGE);
//
//                activity.startActivity(intent, options.toBundle());
//            } else {
                activity.startActivity(intent);
//            }
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