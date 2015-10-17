package com.evento.team2.eventspack.ui.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.soapservice.ServiceEvento;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.EntypoModule;
import com.joanzapata.iconify.fonts.IoniconsModule;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivitySocial extends AppCompatActivity {
    private static final String TAG = "ActivitySocial";

    static {
        Iconify.with(new EntypoModule());
        Iconify.with(new IoniconsModule());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_register_email)
    public void login() {
    }
}
