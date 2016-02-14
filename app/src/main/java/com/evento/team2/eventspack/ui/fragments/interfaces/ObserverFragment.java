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
import com.evento.team2.eventspack.provider.FetchAsyncTask;

import java.util.Observable;
import java.util.Observer;

import butterknife.ButterKnife;

/**
 * Created by Daniel on 29-Oct-15.
 */
public abstract class ObserverFragment extends Fragment implements Observer {

    protected FetchAsyncTask fetchAsyncTask;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (fetchAsyncTask != null) {
            fetchAsyncTask.cancel(true);
            fetchAsyncTask = null;
        }

//        RefWatcher refWatcher = EventiApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectComponent(((EventiApplication) getActivity().getApplication()).getAppComponent());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            final SearchView searchView = (SearchView) searchItem.getActionView();
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

    @Override
    public void update(Observable observable, Object eventsArrayList) {
        // reminescence from the stupid days o.O

//        if (eventsArrayList instanceof ArrayList) {
//            if (eventsAdapter != null) {
//                // TODO ugly solution for a problem which is caused because I use
//                // TODO one fetchasync task for all data fetching
//                eventsAdapter.addEvents((ArrayList<Event>) eventsArrayList);
//                eventsAdapter.notifyDataSetChanged();
//
//                if (!NetworkUtils.getInstance().isNetworkAvailable(EventiApplication.applicationContext)) {
//                    if(getActivity() != null) {
//                        getActivity().runOnUiThread(() -> {
//                            Snackbar.make(eventsRecyclerView,
//                                    R.string.no_internet_connection_cached_events,
//                                    Snackbar.LENGTH_LONG)
//                                    .show();
//                        });
//                    }
//                }
//
//                if (eventsAdapter.getItemCount() > 0) {
//                    if(getActivity() != null) {
//                        getActivity().runOnUiThread(() -> {
//                            if (emptyAdapterTextView != null) {
//                                emptyAdapterTextView.setVisibility(View.GONE);
//                            }
//                        });
//                    }
//                }
//            }
//        }
//
//        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
//            swipeRefreshLayout.setRefreshing(false);
//        }
    }
}
