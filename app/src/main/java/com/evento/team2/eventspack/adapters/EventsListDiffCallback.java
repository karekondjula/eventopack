package com.evento.team2.eventspack.adapters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.evento.team2.eventspack.models.Event;

import java.util.List;

/**
 * Created by d-kareski on 11/26/16.
 */

class EventsListDiffCallback extends DiffUtil.Callback {
    private List<Event> mOldList;
    private List<Event> mNewList;

    EventsListDiffCallback(List<Event> oldList, List<Event> newList) {
        this.mOldList = oldList;
        this.mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList != null ? mOldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return mNewList != null ? mNewList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewList.get(newItemPosition).id == mOldList.get(oldItemPosition).id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewList.get(newItemPosition).equals(mOldList.get(oldItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Event newEvent = mNewList.get(newItemPosition);
        Event oldEvent = mOldList.get(oldItemPosition);

        Bundle diffBundle = new Bundle();

        if (!newEvent.name.equals(oldEvent.name)) {
            diffBundle.putString(Event.Table.COLUMN_NAME, newEvent.name);
        }
        if (!newEvent.details.equals(oldEvent.details)) {
            diffBundle.putString(Event.Table.COLUMN_DETAILS, newEvent.details);
        }
        if (newEvent.startTimeStamp != oldEvent.startTimeStamp) {
            diffBundle.putLong(Event.Table.COLUMN_START_TIME_STAMP, newEvent.startTimeStamp);
        }
        if (newEvent.endTimeStamp != oldEvent.endTimeStamp) {
            diffBundle.putLong(Event.Table.COLUMN_END_TIME_STAMP, newEvent.endTimeStamp);
        }
        if (!newEvent.locationString.equals(oldEvent.locationString)) {
            diffBundle.putString(Event.Table.COLUMN_LOCATION_STRING, newEvent.locationString);
        }
        if (!newEvent.attendingCount.equals(oldEvent.attendingCount)) {
            diffBundle.putString(Event.Table.COLUMN_ATTENDING_COUNT, newEvent.attendingCount);
        }
        if (!newEvent.pictureUri.equals(oldEvent.pictureUri)) {
            diffBundle.putString(Event.Table.COLUMN_PICTURE_URI, newEvent.pictureUri);
        }

        if (diffBundle.size() == 0) return null;

        return diffBundle;
    }
}