package com.exarlabs.android.myrules.business.event;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * @see {@link RuleEventManager} maintains a list of plugins which are listening to some events.
 * If an even is dipatched by any of the plugins this event manager catches the event and dispatches further.
 * It also administers the plugins' state, it can enable and disable them runtime.
 * <p>
 * Created by becze on 1/11/2016.
 */
public class RuleEventManager {

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

    /**
     * List of plugins which are dispatching the events
     */
    private List<EventHandlerPlugin> mPlugins;
    private Observable<Event> mEventObservable;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    /**
     * Creates a rule event manager with the list of plugins
     *
     * @param plugins
     */
    public RuleEventManager(List<EventHandlerPlugin> plugins) {
        mPlugins = plugins;
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    /**
     * Initialize the event manager.
     */
    public void init() {
        List<Observable<Event>> mEventPluginObservables = new ArrayList<>();

        for (EventHandlerPlugin plugin : mPlugins) {
            mEventPluginObservables.add(plugin.getEventObservable());
        }

        mEventObservable = Observable.from(mEventPluginObservables).flatMap(event -> event);
    }


    /**
     * @return true if the EventManager is initialized
     */
    public boolean isInitialized() {
        return mEventObservable != null;
    }


    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------


    /**
     * @return an event observable
     */
    public Observable<Event> getEventObservable() {
        if (!isInitialized()) {
            init();
        }

        return mEventObservable;
    }
}
