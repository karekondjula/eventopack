package com.evento.team2.eventspack.presenters.interfaces;

import com.evento.team2.eventspack.models.Category;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.views.FragmentCategoriesView;

import java.util.List;

/**
 * Created by Daniel on 12-Jan-16.
 */
public interface FragmentCategoriesPresenter {

    void setView(FragmentCategoriesView fragmentCategoriesView);

    void fetchCategoriesWithActiveEvents(String query);
}
