package com.evento.team2.eventspack.presenters;

import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.interactors.interfaces.NotificationsInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.presenters.interfaces.FragmentEventDetailsPresenter;
import com.evento.team2.eventspack.utils.interfaces.MainThread;
import com.evento.team2.eventspack.views.FragmentEventDetailsView;

/**
 * Created by Daniel on 06-Mar-16.
 */
public class FragmentEventDetailsPresenterImpl implements FragmentEventDetailsPresenter {

    private FragmentEventDetailsView fragmentEventDetailsView;
    private MainThread mainThread;
    private DatabaseInteractor databaseInteractor;
    private NotificationsInteractor notificationsInteractor;

    public FragmentEventDetailsPresenterImpl(MainThread mainThread,
                                             DatabaseInteractor databaseInteractor,
                                             NotificationsInteractor notificationsInteractor) {
        this.mainThread = mainThread;
        this.databaseInteractor = databaseInteractor;
        this.notificationsInteractor = notificationsInteractor;
    }

    @Override
    public void setView(FragmentEventDetailsView fragmentEventDetailsView) {
        this.fragmentEventDetailsView = fragmentEventDetailsView;
    }

    @Override
    public void fetchEventDetails(long eventId) {
        new Thread() {
            @Override
            public void run() {
                final Event event = databaseInteractor.getEventById(eventId);
                mainThread.post(() -> fragmentEventDetailsView.showEvent(event));
            }
        }.start();
    }

    @Override
    public void changeSavedStateOfEvent(Event event) {
        event.isEventSaved = !event.isEventSaved;

        databaseInteractor.changeSaveEvent(event, event.isEventSaved);

        if (event.isEventSaved) {
            notificationsInteractor.scheduleNotification(event);
        } else {
            notificationsInteractor.removeScheduleNotification(event);
        }

        fragmentEventDetailsView.notifyUserForUpdateInEvent(event.isEventSaved);
    }
}