package com.exarlabs.android.myrules.business.rule;

import com.exarlabs.android.myrules.business.rule.event.Event;

/**
 * An evaluable condition based on it current state can decide if a certain condition is true or false based on an event.
 * <p>
 *
 * @author becze
 * @see Rule Rule
 * @see com.exarlabs.android.myrules.business.rule.condition.Condition condition
 * <p>
 * Created by becze on 2/1/2016
 */
public interface Evaluable {


    /**
     * Evaluates the component and returns it's logical value.
     *
     * @param event The event which triggered this evaluation.
     * @return true if the evaluation's result is true.
     */
    boolean evaluate(Event event);
}
