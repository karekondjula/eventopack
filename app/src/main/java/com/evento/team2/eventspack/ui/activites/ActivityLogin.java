package com.evento.team2.eventspack.ui.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityLogin extends AppCompatActivity {
    private static final String TAG = "ActivityLogin";
    private static final int REQUEST_SIGNUP = 0;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        signupLink.setText(Html.fromHtml("No account yet? Click <i>here</i>"));
        skipLoginLink.setText(Html.fromHtml("Skip login <i>here</i>"));
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

    @OnClick(R.id.link_signup)
    public void openSignupActivity(View v) {
        // Start the Signup activity
        Intent intent = new Intent(getApplicationContext(), ActivitySignup.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
    }

    @OnClick(R.id.link_skip_login)
    public void skipLogin(View v) {
        // Skip Signup activity
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
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
