package com.evento.team2.eventspack.ui.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evento.team2.eventspack.R;

/**
 * Created by Daniel on 31-Oct-15.
 */
public class DialogFragmentAbout extends DialogFragment {

    public DialogFragmentAbout() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container);
        getDialog().setTitle(getString(R.string.about_eventi));
        return view;
    }

}
