package com.evento.team2.eventspack.ui.interfaces;


import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.provider.FetchAsyncTask;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.IoniconsIcons;

import java.util.Observer;

import butterknife.ButterKnife;

/**
 * Created by Daniel on 29-Oct-15.
 */
public abstract class ObserverFragment extends Fragment implements Observer {

    //    private SearchView searchView = null;
    protected FetchAsyncTask fetchAsyncTask;

    @Override
    public void onDetach() {
        ButterKnife.unbind(this);
        if (fetchAsyncTask != null) {
            fetchAsyncTask.cancel(true);
        }
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            final SearchView searchView = (SearchView) searchItem.getActionView();
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

            if (searchView != null) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
                searchView.setQueryRefinementEnabled(true);
                searchView.setSubmitButtonEnabled(false);
            }
        }

        super.onCreateOptionsMenu(menu, menuInflater);
    }

    public abstract void filterList(String query);
}
