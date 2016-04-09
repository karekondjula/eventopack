package com.evento.team2.eventspack.models;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

/**
 * Created by Daniel on 15-Mar-16.
 */
// TODO try AutoValues
public class Category implements ParentListItem {

    public @Event.Category int categoryId;
    public int categoryNameId;
    private List<Event> events;

    public Category(int categoryId, int categoryNameId, List<Event> events) {
        this.categoryId = categoryId;
        this.categoryNameId = categoryNameId;
        this.events = events;
    }

    @Override
    public List<Event> getChildItemList() {
        return events;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
