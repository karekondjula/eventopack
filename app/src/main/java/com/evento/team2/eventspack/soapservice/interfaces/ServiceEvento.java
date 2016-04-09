package com.evento.team2.eventspack.soapservice.interfaces;

import java.util.HashMap;

/**
 * Created by Daniel on 30-Mar-16.
 */
// TODO make ServiceInterface for all service related methods and do soap magic inside implementation
public interface ServiceEvento {

    void callServiceMethod(HashMap<String, Object> params);
}
