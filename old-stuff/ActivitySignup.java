package com.evento.team2.eventspack.ui.activites;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.soapservice.ServiceEvento;
import com.evento.team2.eventspack.soapservice.model.User;
import com.evento.team2.eventspack.utils.NetworkUtils;

import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

// TODO daniel NOT USED ... REMOVE IT!
public class ActivitySignup {
//        extends AppCompatActivity {
//    private static final String TAG = "ActivitySignup";
//
//    @Bind(R.id.input_name)
//    EditText nameText;
//    @Bind(R.id.input_email)
//    EditText emailText;
//    @Bind(R.id.input_password)
//    EditText passwordText;
//    @Bind(R.id.btn_signup)
//    Button signupButton;
//    @Bind(R.id.link_skip_login)
//    TextView skipLoginLink;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signup);
//        ButterKnife.bind(this);
//
//        skipLoginLink.setText(Html.fromHtml("Skip login <font color=\"#2638C2\"><i>here</i></font>"));
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        ButterKnife.unbind(this);
//    }
//
//    @OnClick(R.id.btn_signup)
//    public void signup(View view) {
//        Log.d(TAG, "Signup");
//
////        if (!NetworkUtils.getInstance().isNetworkAvailable(this)) {
////            Snackbar.make(view,
////                    "No internet connection. Please continue without login in",
////                    Snackbar.LENGTH_LONG)
//////                    .setAction("Undo", null)
//////                    .setActionTextColor(Color.RED)
////                    .show();
////
////            signupButton.setEnabled(true);
////
////            return;
////        }
//
//        if (!validate()) {
//            signupButton.setEnabled(true);
//            return;
//        }
//
//        signupButton.setEnabled(false);
//
//        final ProgressDialog progressDialog = new ProgressDialog(ActivitySignup.this, R.style.MyMaterialTheme);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Creating Account...");
//        progressDialog.show();
//
//        String name = nameText.getText().toString();
//        String email = emailText.getText().toString();
//        String password = passwordText.getText().toString();
//
//        User user = new User();
//        user.timestamp = System.currentTimeMillis();
//        user.email = email;
//        user.name = name;
//        user.username = email;
//        user.password = password;
////        HashMap<String, Object> params = new HashMap<>();
////        params.put(ServiceEvento.METHOD_NAME_KEY, ServiceEvento.METHOD_ADD_USER);
////        try {
////            params.put(ServiceEvento.ADD_USER_USER_REQUEST_KEY, LoganSquare.serialize(user));
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        ServiceEvento.getInstance().callServiceMethod(params);
//
//        // TODO daniel : Implement your own signup logic here.
//
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onSignupSuccess or onSignupFailed
//                        // depending on success
//                        onSignupSuccess();
//                        // onSignupFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
//    }
//
//    @OnClick(R.id.link_skip_login)
//    public void skipLogin(View v) {
//        signupButton.setEnabled(true);
//        setResult(RESULT_FIRST_USER, null);
//        this.finish();
//    }
//
//    public void onSignupSuccess() {
//        signupButton.setEnabled(true);
//        setResult(RESULT_OK, null);
//        finish();
//    }
//
//    public boolean validate() {
//        boolean valid = true;
//
//        String name = nameText.getText().toString();
//        String email = emailText.getText().toString();
//        String password = passwordText.getText().toString();
//
//        if (name.isEmpty() || name.length() < 3) {
//            nameText.setError("at least 3 characters");
//            valid = false;
//        } else {
//            nameText.setError(null);
//        }
//
//        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            emailText.setError("enter a valid email address");
//            valid = false;
//        } else {
//            emailText.setError(null);
//        }
//
//        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
//            passwordText.setError("between 4 and 10 alphanumeric characters");
//            valid = false;
//        } else {
//            passwordText.setError(null);
//        }
//
//        return valid;
//    }
}