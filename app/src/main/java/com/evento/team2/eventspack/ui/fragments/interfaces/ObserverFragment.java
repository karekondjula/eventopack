package com.evento.team2.eventspack.ui.fragments.interfaces;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.components.AppComponent;

import java.util.Observable;
import java.util.Observer;

import butterknife.ButterKnife;

/**
 * Created by Daniel on 29-Oct-15.
 */
public abstract class ObserverFragment extends Fragment {

    private SearchView searchView;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

//        RefWatcher refWatcher = EventiApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectComponent(((EventiApplication) getActivity().getApplication()).getAppComponent());
    }

    @Override
    public void onResume() {
        super.onResume();

        if (searchView != null) {
            searchView.setQuery("", false);
            searchView.onActionViewCollapsed();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint(getString(R.string.filter));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    filterList(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterList(newText);
                    return true;
                }
            });
            searchView.setOnQueryTextFocusChangeListener((view, queryTextFocused) -> {
                if (!queryTextFocused) {
                    if (TextUtils.isEmpty(searchView.getQuery())) {
                        searchItem.collapseActionView();
                        searchView.setIconified(true);
                        searchView.setQuery("", false);
                    }
                }
            });
            // Get the search close button image view
            ButterKnife.findById(searchView, R.id.search_close_btn).setOnClickListener(v -> {
                searchView.setQuery("", false);
                searchView.onActionViewCollapsed();
                searchItem.collapseActionView();
            });
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setQueryRefinementEnabled(true);
            searchView.setSubmitButtonEnabled(false);
        }

        super.onCreateOptionsMenu(menu, menuInflater);
    }

    public abstract void filterList(String query);

    protected abstract void injectComponent(AppComponent component);
}
