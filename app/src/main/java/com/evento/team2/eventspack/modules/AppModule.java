package com.evento.team2.eventspack.modules;

import com.evento.team2.eventspack.EventiApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Daniel on 10-Jan-16.
 */
@Module(includes = {PresentersModule.class, InteractorsModule.class, ThreadsModule.class})
public class AppModule {

    EventiApplication eventiApplication;

    public AppModule(EventiApplication application) {
        eventiApplication = application;
    }

    @Provides
    @Singleton
    EventiApplication providesApplication() {
        return eventiApplication;
    }
}