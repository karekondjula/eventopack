package com.evento.team2.eventspack.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.evento.team2.eventspack.ActivityEventDetails;
import com.evento.team2.eventspack.ActivityMain;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.utils.Utils;
import com.kogitune.activity_transition.ActivityTransitionLauncher;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Daniel on 31-Jul-15.
 */
public class FragmentEvents extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_events_list, container, false);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items

                new Thread() {
                    @Override
                    public void run() {
                        // stop the wheel from turning
                        SystemClock.sleep(2000);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });

                    }
                }.start();
            }
        });

        RecyclerView rv = (RecyclerView) swipeRefreshLayout.findViewById(R.id.recyclerview);
        setupRecyclerView(rv);

        return swipeRefreshLayout;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), Utils.Helpers.createEvents()));
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private ArrayList<Event> events;
        private Context context;

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public final View mView;
            public final ImageView mEventImage;
            public final TextView mEventTitle;
            public final TextView mEventDetails;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mEventImage = (ImageView) view.findViewById(R.id.event_picture);
                mEventTitle = (TextView) view.findViewById(R.id.event_title);
                mEventDetails = (TextView) view.findViewById(R.id.event_details);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mEventTitle.getText();
            }
        }

        public Event getValueAt(int position) {
            return events.get(position);
        }

        public SimpleStringRecyclerViewAdapter(Context context, ArrayList<Event> eventNames) {
            this.context = context;
            this.events = eventNames;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_events, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mEventTitle.setText(events.get(position).name);
            holder.mEventDetails.setText(events.get(position).details);

            if (TextUtils.isEmpty(events.get(position).pictureUri)) {
                Glide.with(holder.mEventImage.getContext()).load(R.drawable.party_image).fitCenter().into(holder.mEventImage);
            } else {
                // TODO daniel implement picture uri as picture
                Glide.with(context).load(new File(events.get(position).pictureUri)).fitCenter().into(holder.mEventImage);
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();

                        Intent intent = new Intent(context, ActivityEventDetails.class);
                        intent.putExtra(ActivityEventDetails.EXTRA_NAME, events.get(position).name);
                        if (!TextUtils.isEmpty(events.get(position).pictureUri)) {
                            intent.putExtra(ActivityEventDetails.EXTRA_PICTURE_URI, events.get(position).name);
                        }

                        ActivityTransitionLauncher.with((Activity) context).from(v).launch(intent);
//                        Intent intent = new Intent(context, ActivityEventDetails.class);
//                        intent.putExtra(ActivityEventDetails.EXTRA_NAME, events.get(position).name);
//                        if (!TextUtils.isEmpty(events.get(position).pictureUri)) {
//                            intent.putExtra(ActivityEventDetails.EXTRA_PICTURE_URI, events.get(position).name);
//                        }
//                        context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return events.size();
        }
    }

    public static FragmentEvents newInstance() {
        FragmentEvents f = new FragmentEvents();
//        Bundle args = new Bundle();
//        f.setArguments(args);
        return f;
    }
}