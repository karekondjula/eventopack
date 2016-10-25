package com.evento.team2.eventspack.presenters;

import com.evento.team2.eventspack.interactors.NotificationsInteractor;
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.utils.interfaces.MainThread;
import com.evento.team2.eventspack.views.FragmentEventDetailsView;

/**
 * Created by Daniel on 06-Mar-16.
 */
public class FragmentEventDetailsPresenter {

    FragmentEventDetailsView fragmentEventDetailsView;
    MainThread mainThread;
    DatabaseInteractor databaseInteractor;
    private NotificationsInteractor notificationsInteractor;

    public FragmentEventDetailsPresenter(MainThread mainThread,
                                             DatabaseInteractor databaseInteractor,
                                             NotificationsInteractor notificationsInteractor) {
        this.mainThread = mainThread;
        this.databaseInteractor = databaseInteractor;
        this.notificationsInteractor = notificationsInteractor;
    }

    public void setView(FragmentEventDetailsView fragmentEventDetailsView) {
        this.fragmentEventDetailsView = fragmentEventDetailsView;
    }

    public void fetchEventDetails(long eventId) {
        new Thread() {
            @Override
            public void run() {
                final Event event = databaseInteractor.getEventById(eventId);
                mainThread.post(() -> fragmentEventDetailsView.showEvent(event));
            }
        }.start();
    }

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