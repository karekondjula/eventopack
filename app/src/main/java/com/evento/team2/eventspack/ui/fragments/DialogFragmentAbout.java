package com.evento.team2.eventspack.ui.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evento.team2.eventspack.BuildConfig;
import com.evento.team2.eventspack.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Daniel on 31-Oct-15.
 */
public class DialogFragmentAbout extends DialogFragment {

    @BindView(R.id.version)
    TextView verison;

    protected Unbinder unbinder;

    public DialogFragmentAbout() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container);

        unbinder = ButterKnife.bind(this, view);

        getDialog().setTitle(getString(R.string.about_eventi));

        verison.setText(String.format(getString(R.string.version_), BuildConfig.VERSION_NAME));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
