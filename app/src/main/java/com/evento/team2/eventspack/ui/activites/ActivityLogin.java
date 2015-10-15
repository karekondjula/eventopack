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

public class ActivityLogin extends AppCompatActivity {
    private static final String TAG = "ActivityLogin";

    private static final int SIGNUP_REQUEST = 0;
    private static final int LOGIN_SUCCESS = 1;
    private static final int LOGIN_FAILED = 2;

    @Bind(R.id.input_email)
    EditText emailText;
    @Bind(R.id.input_password)
    EditText passwordText;
    @Bind(R.id.btn_login)
    Button loginButton;
    @Bind(R.id.link_signup)
    TextView signupLink;
    @Bind(R.id.link_skip_login)
    TextView skipLoginLink;

    private Handler loginHandler;

    static {
        Iconify.with(new EntypoModule());
        Iconify.with(new IoniconsModule());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        signupLink.setText(Html.fromHtml("No account yet? Click <font color=\"#99cc00\"><i>here</i></font>"));
        skipLoginLink.setText(Html.fromHtml("Skip login <font color=\"#2638C2\"><i>here</i></font>"));
    }

    @OnClick(R.id.btn_login)
    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        // TODO daniel implement something more fancy B-)
        final ProgressDialog progressDialog = new ProgressDialog(ActivityLogin.this, R.style.MyMaterialTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = emailText.getText().toString();
        final String password = passwordText.getText().toString();

        HashMap<String, Object> params = new HashMap<>();
        params.put(ServiceEvento.METHOD_NAME_KEY, ServiceEvento.METHOD_GET_USER);
        params.put(ServiceEvento.GET_USER_USERNAME_REQUEST_KEY, email);
        params.put(ServiceEvento.GET_USER_PASSWORD_REQUEST_KEY, password);

        ServiceEvento.getInstance().callServiceMethod(params);

        loginHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.wtf("", "main:" + msg);
            }
        };

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 1000);
    }

    @OnClick(R.id.login_facebook)
    public void loginWithFacebook(IconTextView iconTextView) {
        // TODO daniel add some animation for > L devices
//        iconTextView.setShadowLayer(3, 9, 9, android.R.color.darker_gray);
//        iconTextView.invalidate();
    }

    @OnClick(R.id.login_gmail)
    public void loginWithGmail(IconTextView iconTextView) {
        // TODO daniel add some animation for > L devices
    }

    @OnClick(R.id.link_signup)
    public void openSignupActivity(View v) {
        // Start the Signup activity
        Intent intent = new Intent(this, ActivitySignup.class);
        startActivityForResult(intent, SIGNUP_REQUEST);
    }

    @OnClick(R.id.link_skip_login)
    public void skipLogin(View v) {
        // Skip Signup activity
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SIGNUP_REQUEST) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                this.finish();
            } else if (resultCode == RESULT_FIRST_USER) {

                // TODO: Implement skip login logic here
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}
