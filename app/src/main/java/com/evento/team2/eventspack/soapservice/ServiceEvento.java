package com.evento.team2.eventspack.soapservice;

import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;

/**
 * Created by Daniel on 18-Aug-15.
 */
public class ServiceEvento {
    private static final String TAG = "ServiceEvento";

    private final String NAMESPACE = "urn:eventservice";
    private final String URL = "http://ap.mk/evento/server.php";
    private final String SOAP_ACTION = "http://ap.mk/evento/server.php/";

    // parameter keys
    public static final String METHOD_NAME_KEY = "METHOD_NAME_KEY";
    // end parameter keys

    // methods
    public static final String METHOD_TEST_FUNC = "test_func";
    public static final String METHOD_ADD_USER = "add_user";
    public static final String METHOD_ADD_EVENT = "add_event";
    public static final String METHOD_EDIT_USER = "edit_user";
    public static final String METHOD_EDIT_EVENT = "edit_event";
    public static final String METHOD_GET_CATEGORIES = "get_categories";
    public static final String METHOD_GET_EVENTS_BY_USER = "get_events_by_user";
    public static final String METHOD_GET_EVENTS_BY_CATEGORY = "get_events_by_category";
    public static final String METHOD_GET_USER = "get_user";
    public static final String METHOD_SEARCH_EVENTS = "search_events";
    public static final String METHOD_DELETE_EVENTS = "delete_event";
    // end methods

    private static ServiceEvento instance;

    private ServiceEvento() {
    }

    public static ServiceEvento getInstance() {
        if (instance == null) {
            instance = new ServiceEvento();
        }

        return instance;
    }

    /*
     * Must contain at least <METHOD_NAME_KEY, methodName> pair
     */
    public void callServiceMethod(HashMap<String, Object> params) {
        new AsyncCallWS().execute(params);
    }

    private class AsyncCallWS extends AsyncTask<HashMap<String, Object>, Void, String> {
        @Override
        protected String doInBackground(HashMap<String, Object>... params) {
            Log.i(TAG, "doInBackground");
            return getResponse(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "onPostExecute");
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate");
        }
    }

    private String getResponse(HashMap<String, Object> inputParameters) {
        String methodNameParam = (String) inputParameters.get(METHOD_NAME_KEY);
        String soapActionParam = SOAP_ACTION.concat(methodNameParam);

        inputParameters.remove(METHOD_NAME_KEY);

        //Create request
        SoapObject request = new SoapObject(NAMESPACE, methodNameParam);

        //Property which holds input parameters
        PropertyInfo property = new PropertyInfo();

        for (String key : inputParameters.keySet()) {
            property.setName(key);

            if (inputParameters.get(key) instanceof String) {
                property.setType(String.class);
            } else if (inputParameters.get(key) instanceof Integer) {
                property.setType(Integer.class);
            } else if (inputParameters.get(key) instanceof Double) {
                property.setType(Double.class);
            }

            property.setValue(inputParameters.get(key));
            request.addProperty(property);
        }

        //Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        //Set output SOAP object
        envelope.setOutputSoapObject(request);
        //Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            //Invole web service
            androidHttpTransport.call(soapActionParam, envelope);
            Log.i(TAG, envelope.getResponse().toString());


            return envelope.getResponse().toString();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ERROR";
//            //Get the response
//            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
    }
}

