package com.exarlabs.android.myrules.business.rule.event;

import java.util.Collection;

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

    private static final String TAG = RuleEventManager.class.getSimpleName();


    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    /**
     * List of plugins which are dispatching the events
     */
    private Collection<? extends EventHandlerPlugin> mPlugins;
    private Observable<Event> mEventObservable;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    /**
     * Creates a rule event manager with the list of plugins
     */
    public RuleEventManager(EventPluginManager eventPluginManager) {
        mPlugins = eventPluginManager.getPlugins();
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    /**
     * Initialize the event manager.
     */
    public void init() {
        mEventObservable = Observable.from(mPlugins).flatMap(plugin -> plugin.getEventObservable());
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
