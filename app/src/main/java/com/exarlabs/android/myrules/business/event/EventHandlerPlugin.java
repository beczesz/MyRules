package com.exarlabs.android.myrules.business.event;

import rx.Observable;
import rx.Subscriber;

/**
 * Abstract event plugin which serveres as a base class for all the RuleEventHandler
 * Created by becze on 1/11/2016.
 */
public class EventHandlerPlugin {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    private Observable<Event> mEventObservable;
    private Subscriber<? super Event> mSubscriber;

    private boolean isEnabled = false;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public EventHandlerPlugin() {
        // Initialize the plugin on creation
        init();

        // By default enable the plugin
        enable();
    }


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    /**
     * Dispatches a new event
     * @param event
     */
    public void dispatchEvent(Event event) {
        if (mSubscriber != null && isEnabled) {
            mSubscriber.onNext(event);
        }
    }

    /**
     * Initialize the plugin by preparing an observable
     */
    public void init() {
        mEventObservable = Observable.create(subscriber -> mSubscriber = subscriber);
    }

    public void enable() {
        isEnabled = true;
    }

    public void disable() {
        isEnabled = false;
    }



    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------

    public Observable<Event> getEventObservable() {
        return mEventObservable;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
