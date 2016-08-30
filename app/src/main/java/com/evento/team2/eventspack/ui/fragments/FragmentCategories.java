package com.evento.team2.eventspack.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.adapters.CategoriesViewAdapter;
import com.evento.team2.eventspack.components.AppComponent;
import com.evento.team2.eventspack.models.Category;
import com.evento.team2.eventspack.presenters.interfaces.FragmentCategoriesPresenter;
import com.evento.team2.eventspack.ui.fragments.interfaces.BaseFragment;
import com.evento.team2.eventspack.utils.EventiConstants;
import com.evento.team2.eventspack.views.FragmentCategoriesView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 31-Jul-15.
 */
public class FragmentCategories extends BaseFragment implements FragmentCategoriesView {

    public static final String TAG = "fragmentCategories";

    @Inject
    FragmentCategoriesPresenter fragmentCategoriesPresenter;

    @BindView(R.id.categoriesRecyclerView)
    RecyclerView categoriesRecyclerView;

    private HashSet<Integer> expandedParentsList = new HashSet<>();

    private CategoriesViewAdapter categoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryAdapter = new CategoriesViewAdapter(getActivity(), new ArrayList<>());

        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoriesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        categoriesRecyclerView.setAdapter(categoryAdapter);

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
        if (getActivity() != null) {
            categoryAdapter = new CategoriesViewAdapter(getActivity(), categoryList);

            categoryAdapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
                @Override
                public void onListItemExpanded(int position) {
                    expandedParentsList.add(position);
                }

                @Override
                public void onListItemCollapsed(int position) {
                    expandedParentsList.remove(position);
                }
            });

            if(categoriesRecyclerView != null) {
                categoriesRecyclerView.setAdapter(categoryAdapter);
            }

            if (expandedParentsList != null) {
                for(int expandedParent : expandedParentsList) {
                    categoryAdapter.expandParent(expandedParent);
                }
                categoryAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * This fragment is created in the same time with Events so on first start it will show no
     * categories :(
     * */
    public void refreshViewIfRequired() {
        if (getActivity() != null) {
            if (categoryAdapter != null && categoryAdapter.getItemCount() == 0) {
                fragmentCategoriesPresenter.fetchCategoriesWithActiveEvents(EventiConstants.NO_FILTER_STRING);
            }
        }
    }
}