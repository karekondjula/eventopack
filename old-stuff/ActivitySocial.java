package com.evento.team2.eventspack.ui.activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.evento.team2.eventspack.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.EntypoModule;
import com.joanzapata.iconify.fonts.IoniconsModule;
import com.joanzapata.iconify.widget.IconTextView;

public class ActivitySocial extends AppCompatActivity {
    private static final String TAG = "ActivitySocial";

    static {
//        Iconify.with(new EntypoModule());
//        Iconify.with(new IoniconsModule());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_register_email)
    public void registerEmail() {
    }
}
