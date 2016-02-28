package com.evento.team2.eventspack.modules;

import android.view.View;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.presenters.FragmentCalendarPresenterImpl;
import com.evento.team2.eventspack.presenters.interfaces.FragmentCalendarPresenter;
import com.evento.team2.eventspack.scopes.CalendarScope;
import com.evento.team2.eventspack.utils.ColorUtils;
import com.evento.team2.eventspack.utils.interfaces.MainThread;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Date;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Daniel on 13-Feb-16.
 */
@Module
public class CalendarModule {

    @Provides
    @CalendarScope
    CaldroidFragment provideCaldroidFragment() {
        return new CaldroidFragment();
    }

    @Provides
    @CalendarScope
    FragmentCalendarPresenter provideFragmentCalendarPresenter(MainThread mainThread, DatabaseInteractor databaseInteractor) {
        return new FragmentCalendarPresenterImpl(mainThread, databaseInteractor);
    }

    @Provides
    @CalendarScope
    ColorUtils provideColorUtils() {
        return new ColorUtils();
    }
}