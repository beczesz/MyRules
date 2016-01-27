package com.exarlabs.android.myrules.business.event;

import android.util.Log;

import rx.Observable;
import rx.Subscriber;

/**
 * Abstract event plugin which serveres as a base class for all the RuleEventHandler
 * Created by becze on 1/11/2016.
 */
public abstract class EventHandlerPlugin {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = EventHandlerPlugin.class.getSimpleName();
    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    private Observable<Event> mEventObservable;
    private Subscriber<? super Event> mSubscriber;

    private boolean isEnabled = false;

    /**
     * The type of the plugin
     */
    private int mType;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public EventHandlerPlugin(int type) {
        mType = type;

        // Initialize the plugin on creation
        init();

        // By default enable the plugin
        enable();
    }


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------


    /**
     * Initializes the plugin on creation.
     *
     * @return true if the initialization is successful.
     */
    protected abstract boolean initPlugin();

    /**
     * Dispatches a new event
     *
     * @param event
     */
    protected void dispatchEvent(Event event) {
        if (mSubscriber != null && isEnabled) {
            // TODO implement our own thread pool
            new Thread("Thread for: " + this.getClass().getSimpleName()) {
                @Override
                public void run() {
                    mSubscriber.onNext(event);
                }
            }.start();
        }
    }

    /**
     * Initialize the plugin by preparing an observable
     */
    public void init() {
        mEventObservable = Observable.create(subscriber -> mSubscriber = subscriber);
        if (!initPlugin()) {
            Log.e(TAG, "Plugin could not be initialized");
        }
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

    public int getType() {
        return mType;
    }
}
