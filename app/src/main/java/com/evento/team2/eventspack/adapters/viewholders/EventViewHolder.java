package com.evento.team2.eventspack.adapters.viewholders;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.utils.DateFormatterUtils;
import com.joanzapata.iconify.widget.IconTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by d-kareski on 12/14/16.
 */

public class EventViewHolder extends RecyclerView.ViewHolder {

    public static final String ICON_TEXT_VIEW_FILLED_HEART = "{ion-android-favorite @color/colorPrimary}";
    public static final String ICON_TEXT_VIEW_EMPTY_HEART = "{ion-android-favorite-outline @color/colorPrimary}";
    public static final String ICON_TEXT_VIEW_FILLED_HEART_SPIN = "{ion-android-favorite @color/colorPrimary spin}";
    public static final String ICON_TEXT_VIEW_EMPTY_HEART_SPIN = "{ion-android-favorite-outline @color/colorPrimary spin}";

    public interface EventListener {
        void onHeartClicked(EventViewHolder eventViewHolder);
        void onEventClicked(EventViewHolder eventViewHolder);
    }

    @BindView(R.id.event_picture)
    ImageView mEventImage;
    @BindView(R.id.event_title)
    TextView mEventTitle;
    @BindView(R.id.event_details)
    TextView mEventDetails;
    @BindView(R.id.event_start_time)
    TextView mEventStartTime;
    @BindView(R.id.event_end_time)
    TextView mEventEndTime;
    @BindView(R.id.event_location)
    TextView mEventLocation;
    @BindView(R.id.btn_save_icon)
    public IconTextView isEventSaved;
    @BindView(R.id.eventAttending)
    View mEventAttending;
    @BindView(R.id.eventAttendingCount)
    TextView mEventAttendingCount;

    private Context context;
    private Event event;
    private Calendar calendar;

    public EventViewHolder(View view, Context context, Calendar calendar, EventListener eventListener) {
        super(view);
        this.context = context;
        this.calendar = calendar;

        ButterKnife.bind(this, view);

        isEventSaved.setOnClickListener(v -> {

            eventListener.onHeartClicked(this);
        });

        view.setOnClickListener(v -> {

            eventListener.onEventClicked(this);
        });
    }

    public void setItem(Event event) {
        this.event = event;

        mEventTitle.setText(event.name);
        mEventDetails.setText(event.details);

        try {
            Date startDateEvent, endDateEvent;
            startDateEvent = DateFormatterUtils.compareDateFormat.parse(event.startDateString);

            if (Math.abs(startDateEvent.getTime() - calendar.getTimeInMillis()) < 1000) {
                // today (or close enough)
//              TODO  DateUtils.getRelativeDateTimeString(application, timestamp, DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0).toString();

                mEventStartTime.setText(event.startTimeStamp != 0 ? context.getString(R.string.today)
                        + " " + DateFormatterUtils.hoursMinutesDateFormat.format(event.startTimeStamp) : "");
                mEventStartTime.setTextColor(context.getResources().getColor(R.color.colorAccent));
            } else if (startDateEvent.getTime() - calendar.getTimeInMillis() < 0) {
                // event has expired
                mEventStartTime.setText(event.startTimeStamp != 0 ? DateFormatterUtils.fullDateFormat.format(event.startTimeStamp)
                        : "");
                mEventStartTime.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            } else {
                // event is on a day != from today
                mEventStartTime.setText(event.startTimeStamp != 0 ? DateFormatterUtils.fullDateFormat.format(event.startTimeStamp)
                        : "");
                mEventStartTime.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            }

            // TODO refactor
            if (event.endTimeStamp != 0) {
                endDateEvent = DateFormatterUtils.compareDateFormat.parse(event.endDateString);
                if (event.endTimeStamp - calendar.getTimeInMillis() < 0) {
                    // end time has also expired
                    mEventEndTime.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                } else {
                    if (Math.abs(endDateEvent.getTime() - calendar.getTimeInMillis()) < 1000) {
                        // end time is today
                        mEventEndTime.setText(context.getString(R.string.today) + " " + DateFormatterUtils.hoursMinutesDateFormat.format(event.endTimeStamp));
                        mEventEndTime.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    } else {
                        // end time is still active
                        mEventEndTime.setText(DateFormatterUtils.fullDateFormat.format(event.endTimeStamp));
                        mEventEndTime.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                    }
                }
            } else {
                mEventEndTime.setText("");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(event.pictureUri)) {
            Glide.with(mEventImage.getContext()).load(R.drawable.party_image).into(mEventImage);
        } else {
            Glide.with(context).load(event.pictureUri).into(mEventImage);
        }

        mEventLocation.setText(event.locationString);

        if (event.isEventSaved) {
            isEventSaved.setText(ICON_TEXT_VIEW_FILLED_HEART);
        } else {
            isEventSaved.setText(ICON_TEXT_VIEW_EMPTY_HEART);
        }

        if (!TextUtils.isEmpty(event.attendingCount)) {
            mEventAttendingCount.setText(event.attendingCount);
        } else {
            mEventAttending.setVisibility(View.GONE);
        }
    }

    public void setItem(List<Object> payloads) {
        Bundle bundle = (Bundle) payloads.get(0);
        for (String key : bundle.keySet()) {
            switch (key) {
                case Event.Table.COLUMN_NAME:
                    mEventTitle.setText(bundle.getString(key));
                    break;
                case Event.Table.COLUMN_DETAILS:
                    mEventDetails.setText(bundle.getString(key));
                    break;
                case Event.Table.COLUMN_START_TIME_STAMP:

                    long startTimeStamp = bundle.getLong(key);
                    String dateTime = new SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.getDefault()).format(bundle.getLong(key));
                    String startDateString = dateTime.split(" ")[1];

                    Date startDateEvent;
                    try {
                        startDateEvent = DateFormatterUtils.compareDateFormat.parse(startDateString);

                        if (Math.abs(startDateEvent.getTime() - calendar.getTimeInMillis()) < 1000) {
                            // today (or close enough)
//              TODO  DateUtils.getRelativeDateTimeString(application, timestamp, DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0).toString();

                            mEventStartTime.setText(startTimeStamp != 0 ? context.getString(R.string.today)
                                    + " " + DateFormatterUtils.hoursMinutesDateFormat.format(startTimeStamp) : "");
                            mEventStartTime.setTextColor(context.getResources().getColor(R.color.colorAccent));
                        } else if (startDateEvent.getTime() - calendar.getTimeInMillis() < 0) {
                            // event has expired
                            mEventStartTime.setText(startTimeStamp != 0 ? DateFormatterUtils.fullDateFormat.format(startTimeStamp)
                                    : "");
                            mEventStartTime.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                        } else {
                            // event is on a day != from today
                            mEventStartTime.setText(startTimeStamp != 0 ? DateFormatterUtils.fullDateFormat.format(startTimeStamp)
                                    : "");
                            mEventStartTime.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case Event.Table.COLUMN_END_TIME_STAMP:

                    long endTimeStamp = bundle.getLong(key);
                    String endDateString = DateFormatterUtils.compareDateFormat.format(endTimeStamp);

                    if (endTimeStamp != 0) {
                        try {
                            Date endDateEvent = DateFormatterUtils.compareDateFormat.parse(endDateString);

                            if (endTimeStamp - calendar.getTimeInMillis() < 0) {
                                // end time has also expired
                                mEventEndTime.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                            } else {
                                if (Math.abs(endDateEvent.getTime() - calendar.getTimeInMillis()) < 1000) {
                                    // end time is today
                                    mEventEndTime.setText(context.getString(R.string.today) + " " +
                                            DateFormatterUtils.hoursMinutesDateFormat.format(endTimeStamp));
                                    mEventEndTime.setTextColor(context.getResources().getColor(R.color.colorAccent));
                                } else {
                                    // end time is still active
                                    mEventEndTime.setText(DateFormatterUtils.fullDateFormat.format(endTimeStamp));
                                    mEventEndTime.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    } else {
                        mEventEndTime.setText("");
                    }
                    break;

                case Event.Table.COLUMN_LOCATION_STRING:
                    mEventLocation.setText(bundle.getString(key));
                    break;

                case Event.Table.COLUMN_ATTENDING_COUNT:
                    mEventAttendingCount.setText(bundle.getString(key));
                    mEventAttending.setVisibility(View.VISIBLE);
                    break;

                case Event.Table.COLUMN_PICTURE_URI:
                    if (TextUtils.isEmpty(bundle.getString(key))) {
                        Glide.with(mEventImage.getContext()).load(R.drawable.party_image).into(mEventImage);
                    } else {
                        Glide.with(context).load(bundle.getString(key)).into(mEventImage);
                    }
                    break;
            }
        }
    }

    public Event getEvent() {
        return event;
    }

    public IconTextView getIsEventSaved() {
        return isEventSaved;
    }
}