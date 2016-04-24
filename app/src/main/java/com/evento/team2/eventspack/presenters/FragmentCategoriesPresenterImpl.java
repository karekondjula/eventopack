package com.evento.team2.eventspack.presenters;

import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.models.Category;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.presenters.interfaces.FragmentCategoriesPresenter;
import com.evento.team2.eventspack.utils.ConversionUtils;
import com.evento.team2.eventspack.utils.interfaces.MainThread;
import com.evento.team2.eventspack.views.FragmentCategoriesView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 10-Jan-16.
 */
public class FragmentCategoriesPresenterImpl implements FragmentCategoriesPresenter {

    private FragmentCategoriesView fragmentCategoriesView;
    private MainThread mainThread;
    private DatabaseInteractor databaseInteractor;

    private String lastQuery = "";

    public FragmentCategoriesPresenterImpl(MainThread mainThread, DatabaseInteractor databaseInteractor) {
        this.mainThread = mainThread;
        this.databaseInteractor = databaseInteractor;
    }

    @Override
    public void setView(FragmentCategoriesView fragmentCategoriesView) {
        this.fragmentCategoriesView = fragmentCategoriesView;
    }

    @Override
    public void fetchCategoriesWithActiveEvents(String query) {

        new Thread() {
            @Override
            public void run() {
                List<Event> events;
                Category category;
                List<Category> categories = new ArrayList<>();

                // must be changed when new categories are added :/
                for (int i = 0; i < Event.CONCERTS; i++) {

                    events = databaseInteractor.getActiveEventsByCategory(Event.getCategoryByInt(i), lastQuery);
                    if (events.size() > 0) {
                        category = new Category(events.get(0).categoryId, ConversionUtils.getCategoryNameIdByCategoryId(events.get(0).categoryId), events);
                        categories.add(category);
                    }
                }

                events = databaseInteractor.getActiveEventsByCategory(Event.getCategoryByInt(Event.OTHER), lastQuery);
                if (events.size() > 0) {
                    category = new Category(events.get(0).categoryId, ConversionUtils.getCategoryNameIdByCategoryId(events.get(0).categoryId), events);
                    categories.add(category);
                }

                mainThread.post(() -> fragmentCategoriesView.showCategories(categories));
            }
        }.start();

        lastQuery = query;
    }

//    @Override
//    public void setView(FragmentPlacesView fragmentEventsView) {
//        this.fragmentCategoriesView = fragmentEventsView;
//    }
//
//    @Override
//    public void fetchPlaces(String query) {
//        new Thread() {
//            @Override
//            public void run() {
//
//                final ArrayList<Place> placeArrayList = databaseInteractor.getPlaces(lastQuery);
//                mainThread.post(() -> fragmentCategoriesView.showPlaces(placeArrayList));
//            }
//        }.start();
//
//        lastQuery = query;
//    }
}