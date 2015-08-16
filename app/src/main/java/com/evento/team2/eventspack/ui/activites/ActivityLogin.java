package com.evento.team2.eventspack.ui.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.soapservice.GetCityWeatherByZIP;
import com.evento.team2.eventspack.soapservice.GetCategories;
import com.evento.team2.eventspack.soapservice.GetCategoriesResponse;
import com.evento.team2.eventspack.soapservice.WeatherReturn;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.joaocruz04.lib.SOAPManager;
import pt.joaocruz04.lib.misc.JSoapCallback;
import pt.joaocruz04.lib.misc.JsoapError;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), ActivitySignup.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        new Thread() {

            private void getWeatherByZip() {
                String url = "http://www.webservicex.com/globalweather.asmx";
                String namespace = "http://www.webserviceX.NET";
                String method = "GetCitiesByCountry";
                String soap_action = "http://www.webserviceX.NET/GetCitiesByCountry";

                SOAPManager.get(namespace, url, method, soap_action, new GetCityWeatherByZIP("Macedonia"), WeatherReturn.class, new JSoapCallback() {

                    @Override
                    public void onSuccess(Object result) {
                        WeatherReturn res = (WeatherReturn) result;
                        Log.i(">>", res.toString());
                    }

                    @Override
                    public void onError(int error) {
                        switch (error) {
                            case JsoapError.NETWORK_ERROR:
                                Log.v("JSoapExample", "Network error");
                                break;
                            case JsoapError.PARSE_ERROR:
                                Log.v("JSoapExample", "Parsing error");
                                break;
                            default:
                                Log.v("JSoapExample", "Unknown error");
                                break;
                        }
                    }
                });

            }

            private void getCategories() {
                String url = "http://ap.mk/evento/server.php?wsdl";
                String namespace = "http://ap.mk/evento/server.php";
                String method = "get_categories";
                String soap_action = "http://ap.mk/evento/server.php/get_categories";

                Log.i(">>", ">>>>>>>>>>>>>>");
                SOAPManager.get(namespace, url, method, soap_action, new GetCategories(1), GetCategoriesResponse.class, new JSoapCallback() {

                    @Override
                    public void onSuccess(Object result) {
                        GetCategoriesResponse res = (GetCategoriesResponse) result;
                        Log.v(">>", res.toString());
                    }

                    @Override
                    public void onError(int error) {
                        switch (error) {
                            case JsoapError.NETWORK_ERROR:
                                Log.v("JSoapExample", "Network error");
                                break;
                            case JsoapError.PARSE_ERROR:
                                Log.v("JSoapExample", "Parsing error");
                                break;
                            default:
                                Log.v("JSoapExample", "Unknown error: " + error);
                                break;
                        }
                    }

                });
            }

            @Override
            public void run() {
//                String METHOD_NAME = "search_events";
//                String URL = "http://ap.mk/evento/server.php?wsdl";
//                String SOAP_ACTION = "http://ap.mk/evento/server.php/search_events";
//                String NAMESPACE = "http://ap.mk/evento/server.php";

//                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//                PropertyInfo fromProp = new PropertyInfo();
//                fromProp.setName("name");
//                fromProp.setValue(fromCurrency);
//                fromProp.setType(String.class);
//                request.addProperty(fromProp);
//
//                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//                envelope.dotNet = true;
//                envelope.setOutputSoapObject(request);
//                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
//
//                try {
//                    androidHttpTransport.call(SOAP_ACTION, envelope);
//                    SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
//                    String webResponse = response.toString();
//
//                    Log.i(">>", webResponse);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (XmlPullParserException e) {
//                    e.printStackTrace();
//                }

//                getWeatherByZip();
                getCategories();
            }
        }.start();
    }

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

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

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
