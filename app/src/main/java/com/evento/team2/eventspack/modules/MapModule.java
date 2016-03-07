package com.evento.team2.eventspack.modules;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.presenters.FragmentMapPresenterImpl;
import com.evento.team2.eventspack.presenters.interfaces.FragmentMapPresenter;
import com.evento.team2.eventspack.scopes.CalendarScope;
import com.evento.team2.eventspack.scopes.MapScope;
import com.evento.team2.eventspack.utils.interfaces.MainThread;
import com.roomorama.caldroid.CaldroidFragment;

import java.util.Calendar;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Daniel on 07-Mar-16.
 */
@Module
public class MapModule {

    @Provides
    @MapScope
    CaldroidFragment provideCaldroidFragment() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return CaldroidFragment.newInstance("", calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
    }

    @Provides
    @MapScope
    FragmentMapPresenter provideFragmentMapPresenter(EventiApplication eventiApplication,
                                                     DatabaseInteractor databaseInteractor, MainThread mainThread) {
        return new FragmentMapPresenterImpl(eventiApplication, databaseInteractor, mainThread);
    }
}
