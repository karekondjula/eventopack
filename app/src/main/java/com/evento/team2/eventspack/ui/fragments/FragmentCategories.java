package com.evento.team2.eventspack.ui.fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.adapters.CategoryExpandableRecyclerViewAdapter;
import com.evento.team2.eventspack.components.AppComponent;
import com.evento.team2.eventspack.models.Category;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.presenters.interfaces.FragmentCategoriesPresenter;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.evento.team2.eventspack.ui.fragments.interfaces.BaseFragment;
import com.evento.team2.eventspack.utils.EventiConstants;
import com.evento.team2.eventspack.views.FragmentCategoriesView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 31-Jul-15.
 */
public class FragmentCategories extends BaseFragment implements FragmentCategoriesView {

    @Inject
    FragmentCategoriesPresenter fragmentCategoriesPresenter;

    @Bind(R.id.categoriesRecyclerView)
    RecyclerView categoriesRecyclerView;

    private CategoryExpandableRecyclerViewAdapter categoryExpandableRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryExpandableRecyclerViewAdapter = new CategoryExpandableRecyclerViewAdapter(getActivity(), new ArrayList<>());

        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoriesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        categoriesRecyclerView.setAdapter(categoryExpandableRecyclerViewAdapter);

        categoriesRecyclerView.setHasFixedSize(true);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoriesRecyclerView.setItemAnimator(new DefaultItemAnimator());

        fragmentCategoriesPresenter.setView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentCategoriesPresenter.fetchCategoriesWithActiveEvents(EventiConstants.NO_FILTER_STRING);
    }

    public static FragmentCategories newInstance() {
        return new FragmentCategories();
    }

    @Override
    public void filterList(final String filter) {
        fragmentCategoriesPresenter.fetchCategoriesWithActiveEvents(filter);
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @Override
    public void showCategories(List<Category> categoryList) {
        categoryExpandableRecyclerViewAdapter = new CategoryExpandableRecyclerViewAdapter(getActivity(), categoryList);
        // TODO what can we do about this
//        categoriesRecyclerView.swapAdapter(categoryExpandableRecyclerViewAdapter, false);
        if(categoriesRecyclerView != null) {
            categoriesRecyclerView.setAdapter(categoryExpandableRecyclerViewAdapter);
        }
    }
}