package com.evento.team2.eventspack.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.evento.team2.eventspack.R;

/**
 * Created by Daniel on 31-Jul-15.
 */
public class FragmentMapWithEvents extends Fragment {

    public FragmentMapWithEvents() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        rootView.findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Had a snack at Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Undo", null)
                        .setActionTextColor(Color.RED)
                        .show();

                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .playOn(getView().findViewById(R.id.textView));
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public static FragmentMapWithEvents newInstance() {
        FragmentMapWithEvents f = new FragmentMapWithEvents();
        return f;
    }
}