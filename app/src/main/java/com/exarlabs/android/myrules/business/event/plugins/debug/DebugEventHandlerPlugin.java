package com.exarlabs.android.myrules.business.event.plugins.debug;

import java.util.concurrent.TimeUnit;

import com.exarlabs.android.myrules.business.event.Event;
import com.exarlabs.android.myrules.business.event.EventFactory;
import com.exarlabs.android.myrules.business.event.EventHandlerPlugin;

import rx.Observable;

/**
 * Just a timer which displatches an event in every second.
 * Created by becze on 1/11/2016.
 */
public class DebugEventHandlerPlugin extends EventHandlerPlugin {

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

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public DebugEventHandlerPlugin() {
        super();
    }


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public void init() {
        super.init();

        Observable.timer(1, TimeUnit.SECONDS).repeat().subscribe(aLong -> {
            DebugEvent event = (DebugEvent) EventFactory.create(Event.Type.RULE_EVENT_DEBUG);
            event.setDebugEventData1("Debug event " + aLong);
            event.setDebugEventData1("Current time" + System.currentTimeMillis());

            dispatchEvent(event);
        });
    }


    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
