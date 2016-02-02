package com.exarlabs.android.myrules.business.rule;

import com.exarlabs.android.myrules.business.rule.event.Event;

/**
 * Represents a component which can be executed like a task when an event happens.
 *
 * @see com.exarlabs.android.myrules.business.rule.event.Event Event
 * @see Rule Rule
 * <p>
 * Created by becze on 2/1/2016.
 */
public interface Runnable {

    /**
     * Executes a task on the given event which just happened.
     * <p><b>Note:</b> The execution can be an expensive operation so it should be called from a background thread. </p>
     *
     * @param event
     */
    boolean run(Event event);

}
