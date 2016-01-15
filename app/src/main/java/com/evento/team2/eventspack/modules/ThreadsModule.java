package com.evento.team2.eventspack.modules;

import com.evento.team2.eventspack.utils.MainThreadImpl;
import com.evento.team2.eventspack.utils.interfaces.MainThread;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Daniel on 15-Jan-16.
 */
@Module
public class ThreadsModule {

    @Provides
    @Singleton
    MainThread provideMainThread() {
        return new MainThreadImpl();
    }
}
