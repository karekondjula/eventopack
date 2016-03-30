package com.evento.team2.eventspack.models;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

/**
 * Created by Daniel on 15-Mar-16.
 */
public class Category implements ParentListItem {

    public String name;
    private List<Event> events;

    public Category(String name, List<Event> events) {
        this.name = name;
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
