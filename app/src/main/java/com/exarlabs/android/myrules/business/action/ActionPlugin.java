package com.exarlabs.android.myrules.business.action;

import java.util.List;

import com.exarlabs.android.myrules.business.event.Event;
import com.exarlabs.android.myrules.model.dao.RuleActionProperty;

/**
 * An action plugin is the implementation of an action which can be run.
 * based on a Event.
 * <p/>
 * Created by becze on 12/18/2015.
 */
public interface ActionPlugin {

    /**
     * Initializes the plugin with the saved properties
     *
     * @param properties
     */
    void initialize(List<RuleActionProperty> properties);

    /**
     * Generates the list of properties what this plugin needs to be persisted.
     *
     * @return the list of all the propoerties needed.
     */
    List<RuleActionProperty> generateProperties();

    /**
     * Evaluates the condition based on the event wether it is true or false.
     *
     * @param event
     * @return the result of the evaluation. True if the condition holds and false otherwise
     */
    boolean run(Event event);

}
