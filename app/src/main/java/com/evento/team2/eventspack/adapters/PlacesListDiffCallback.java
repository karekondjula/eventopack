package com.evento.team2.eventspack.adapters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.models.Place;

import java.util.List;

/**
 * Created by d-kareski on 11/26/16.
 */

class PlacesListDiffCallback extends DiffUtil.Callback {
    private List<Place> mOldList;
    private List<Place> mNewList;

    PlacesListDiffCallback(List<Place> oldList, List<Place> newList) {
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
        Place newPlace = mNewList.get(newItemPosition);
        Place oldPlace = mOldList.get(oldItemPosition);

        Bundle diffBundle = new Bundle();

        if (!newPlace.name.equals(oldPlace.name)) {
            diffBundle.putString(Event.Table.COLUMN_NAME, newPlace.name);
        }
        if (!newPlace.details.equals(oldPlace.details)) {
            diffBundle.putString(Event.Table.COLUMN_DETAILS, newPlace.details);
        }
        if (!newPlace.locationString.equals(oldPlace.locationString)) {
            diffBundle.putString(Event.Table.COLUMN_LOCATION_STRING, newPlace.locationString);
        }
        if (!newPlace.pictureUri.equals(oldPlace.pictureUri)) {
            diffBundle.putString(Event.Table.COLUMN_PICTURE_URI, newPlace.pictureUri);
        }

        if (diffBundle.size() == 0) return null;

        return diffBundle;
    }
}