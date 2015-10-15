package com.evento.team2.eventspack.ui.fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.adapter.EventsRecyclerViewAdapter;
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.utils.Utils;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.IoniconsModule;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 31-Jul-15.
 */
public class FragmentEvents extends Fragment {

    @Bind(R.id.eventsRecyclerView)
    RecyclerView eventsRecyclerView;

    private EventsRecyclerViewAdapter eventsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_events_list, container, false);
        ButterKnife.bind(this, swipeRefreshLayout);
        Iconify.with(new IoniconsModule());

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items

                new Thread() {
                    @Override
                    public void run() {
                        // TODO daniel fetch new events from almighty service

                        // stop the wheel from turning
                        SystemClock.sleep(1000);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                eventsAdapter.addEvent(new Event("Brand new event", "FUck yeah!"));
//                                eventsAdapter.notifyItemRangeInserted(0, 6);
                                eventsAdapter.notifyItemInserted(0);

                                swipeRefreshLayout.setRefreshing(false);

                                if (((LinearLayoutManager) eventsRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0) {
                                    eventsRecyclerView.getLayoutManager().scrollToPosition(0);
                                }
                            }
                        });

                    }
                }.start();
            }
        });

        eventsAdapter = new EventsRecyclerViewAdapter(getActivity(), Utils.Helpers.createEvents());
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        eventsRecyclerView.setHasFixedSize(true);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        eventsRecyclerView.setAdapter(eventsAdapter);

        return swipeRefreshLayout;
    }

    public static FragmentEvents newInstance() {
        FragmentEvents f = new FragmentEvents();
//        Bundle args = new Bundle();
//        f.setArguments(args);
        return f;
    }
}