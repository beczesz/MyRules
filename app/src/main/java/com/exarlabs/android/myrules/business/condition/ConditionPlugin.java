package com.exarlabs.android.myrules.business.condition;

import java.util.List;

import com.exarlabs.android.myrules.business.event.Event;
import com.exarlabs.android.myrules.model.dao.RuleConditionProperty;

/**
 * A condition plugin is the implementation of a condition which can evaluate itself
 * based on a Event.
 * <p/>
 * Created by becze on 12/18/2015.
 */
public interface ConditionPlugin {

    /**
     * Initializes the plugin with the saved properties
     *
     * @param properties
     */
    void initialize(List<RuleConditionProperty> properties);

    /**
     * Generates the list of properties what this plugin needs to be persisted.
     *
     * @return the list of all the propoerties needed.
     */
    List<RuleConditionProperty> generateProperties();

    /**
     * Evaluates the condition based on the event wether it is true or false.
     *
     * @param event
     * @return the result of the evaluation. True if the condition holds and false otherwise
     */
    boolean evaluate(Event event);

}
