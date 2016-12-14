package com.evento.team2.eventspack.adapters;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.adapters.viewholders.EventViewHolder;
import com.evento.team2.eventspack.models.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Daniel on 12-Aug-15.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private final static Date TODAY = new Date();

    private Context context;
    private ArrayList<Event> events;
    private Calendar calendar;
    private EventViewHolder.EventListener eventListener;

    public EventsAdapter(Context context, EventViewHolder.EventListener eventListener) {
        this.context = context;
        this.eventListener = eventListener;

        events = new ArrayList<>();

        calendar = Calendar.getInstance();
        calendar.setTime(TODAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
    }

    public void updateView(ArrayList<Event> eventsArrayList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                new EventsListDiffCallback(events, eventsArrayList));
        diffResult.dispatchUpdatesTo(this);

        events = eventsArrayList;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view, context, calendar, eventListener);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position, List<Object> payloads) {
        if(!payloads.isEmpty()) {
           holder.setItem(payloads);
        } else {
            onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onBindViewHolder(final EventViewHolder holder, final int position) {

        final Event event = events.get(position);
        holder.setItem(event);
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