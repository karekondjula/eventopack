package com.evento.team2.eventspack.soapservice;

import com.bluelinelabs.logansquare.LoganSquare;
import com.evento.team2.eventspack.BuildConfig;
import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.soapservice.interfaces.ServiceEvento;
import com.evento.team2.eventspack.soapservice.models.JsonEvent;
import com.evento.team2.eventspack.utils.ConversionUtils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Daniel on 18-Aug-15.
 */
public class ServiceEventoImpl implements ServiceEvento {

    private static final String TAG = ">> ServiceEventoImpl";

    // parameter keys
    public static final String METHOD_NAME_KEY = "method_name_key";
    public static final String ADD_USER_USER_REQUEST_KEY = "user";
    public static final String GET_USER_USERNAME_REQUEST_KEY = "username";
    public static final String GET_USER_PASSWORD_REQUEST_KEY = "password";
    public static final String RESPONSE_KEY = "response_key";
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
    public static final String METHOD_GET_ALL_EVENTS = "get_all_events";
    public static final String METHOD_GET_ALL_PLACES = "get_all_places";
    // end methods

    private EventiApplication eventiApplication;
    private DatabaseInteractor databaseInteractor;

    public ServiceEventoImpl(EventiApplication eventiApplication,  DatabaseInteractor databaseInteractor) {
        this.eventiApplication = eventiApplication;
        this.databaseInteractor = databaseInteractor;
    }

    /**
     * Must contain at least [ServiceEventoImpl.METHOD_NAME_KEY, methodName] pair
     */
    private void callServiceMethod(HashMap<String, Object> params) {
//        new AsyncCallWS().execute(params);

        HashMap<String, Object> responseHashMap = getResponse(params);
        parseResponse(responseHashMap);
    }

    private HashMap<String, Object> getResponse(HashMap<String, Object> inputParameters) {
        String methodNameParam = (String) inputParameters.get(METHOD_NAME_KEY);
        String soapActionParam = BuildConfig.SOAP_ACTION;//SOAP_ACTION.concat(methodNameParam);

        HashMap<String, Object> responseMap = new HashMap<>();

        responseMap.put(METHOD_NAME_KEY, inputParameters.get(METHOD_NAME_KEY));

        inputParameters.remove(METHOD_NAME_KEY);

        //Create request
        SoapObject request = new SoapObject(BuildConfig.NAMESPACE, methodNameParam);

        //Property which holds input parameters
        PropertyInfo property = new PropertyInfo();
        for (String key : inputParameters.keySet()) {
            property.setName(key);

            if (inputParameters.get(key) instanceof String) {
                property.setType(String.class);
            } else if (inputParameters.get(key) instanceof Integer) {
                property.setType(Integer.class);
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
        HttpTransportSE androidHttpTransport = new HttpTransportSE(BuildConfig.URL);
        androidHttpTransport.debug = false;
        try {
            androidHttpTransport.call(soapActionParam, envelope);
//            Log.i(TAG, envelope.getResponse().toString());

            responseMap.put(RESPONSE_KEY, envelope.getResponse().toString());

            return responseMap;
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void parseResponse(HashMap<String, Object> responseMap) {
        try {
            if (responseMap != null) {
                if (responseMap.get(METHOD_NAME_KEY).equals(METHOD_ADD_USER)) {
                    Boolean result = LoganSquare.parse((String) responseMap.get(RESPONSE_KEY), Boolean.class);
//                        Log.i(TAG, "METHOD_ADD_USER " + result.toString());
                } else if (responseMap.get(METHOD_NAME_KEY).equals(METHOD_GET_USER)) {
//                        Boolean result = LoganSquare.parse((String) responseMap.get(RESPONSE_KEY), Boolean.class);
//                        Log.i(TAG, "METHOD_GET_USER " + responseMap.get(RESPONSE_KEY));
                } else if (responseMap.get(METHOD_NAME_KEY).equals(METHOD_GET_ALL_EVENTS)) {
                    ArrayList<JsonEvent> jsonEventArrayList = new ArrayList<>(LoganSquare.parseList((String) responseMap.get(RESPONSE_KEY), JsonEvent.class));

                    Event event;
                    for (JsonEvent jsonEvent : jsonEventArrayList) {
                        event = ConversionUtils.convertJsonEventToEvent(jsonEvent);
                        databaseInteractor.persistEvent(event);
                        databaseInteractor.persistPlace(ConversionUtils.extractPlaceFromEvent(event));
                    }

//                    Observable<JsonEvent> jsonEventsObservable = Observable.from(jsonEventArrayList);
//
//                    jsonEventsObservable
//                            .subscribe(jsonEvent -> {
//                                Event event = ConversionUtils.convertJsonEventToEvent(jsonEvent);
//                                databaseInteractor.persistEvent(event);
//
//                                Place place = ConversionUtils.extractPlaceFromEvent(event);
//                                databaseInteractor.persistPlace(place);
//                            });

                }
//                else if (responseMap.get(METHOD_NAME_KEY).equals(METHOD_GET_ALL_PLACES)) {}
            }
//            else {
//                EventsDatabase.getInstance().persistEvents(Utils.Helpers.createEvents());
//                Log.i(TAG, "no response ;( ");
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAllCurrentEvents() {
        HashMap<String, Object> params = new HashMap<>();
        params.put(ServiceEventoImpl.METHOD_NAME_KEY, ServiceEventoImpl.METHOD_GET_ALL_EVENTS);
        callServiceMethod(params);
    }
}