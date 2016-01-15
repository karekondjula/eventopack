package com.evento.team2.eventspack.modules;

import com.evento.team2.eventspack.EventiApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Daniel on 10-Jan-16.
 */
@Module(includes = InteractorsModule.class)
public class AppModule {

    EventiApplication mApplication;

    public AppModule(EventiApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    EventiApplication providesApplication() {
        return mApplication;
    }
}