package com.exarlabs.android.myrules.business.action;

import java.util.ArrayList;
import java.util.List;

import com.exarlabs.android.myrules.business.event.Event;
import com.exarlabs.android.myrules.model.dao.RuleActionProperty;

/**
 * A ActionPlugin is an implementation of the behaviour of a RuleAction.
 * Is is initialized with a list of Propoerties, and this plugin, can modify, delete add new propoerties.
 * Based on these properties is can evaluate itself at any time.
 * <p/>
 * Created by becze on 12/18/2015.
 */
public abstract class ActionPlugin {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = ActionPlugin.class.getSimpleName();


    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    // List of properties
    private List<RuleActionProperty> mProperties;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public ActionPlugin() {
        mProperties = new ArrayList<>();
    }


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    /**
     * Runs the action on the current thread, based on the event
     *
     * @param event
     * @return the result of the evaluation. True if the action holds and false otherwise
     */
    public abstract boolean run(Event event);

    /**
     * Initializes the plugin with the saved properties
     *
     * @param properties
     */
    public void initialize(List<RuleActionProperty> properties) {
        for (RuleActionProperty property : properties) {
            saveProperty(property.getKey(), property.getValue());
        }
    }



    /**
     *
     * @param key
     * @return returns true if we have the given property with the given key
     */
    public boolean hasProperty(String key) {
        if (mProperties != null) {
            for (RuleActionProperty property : mProperties) {
                if (property.getKey().equals(key)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns the property with the given key
     * @param key
     * @return
     */
    public RuleActionProperty getProperty (String key) {

        if (mProperties != null) {
            for (RuleActionProperty property : mProperties) {
                if (property.getKey().equals(key)) {
                    return property;
                }
            }
        }

        return null;
    }

    /**
     * Adds/updates the property with the given key/value.
     * @param key
     * @param value
     */
    public void saveProperty(String key, String value) {
        if (mProperties != null) {
            if (hasProperty(key)) {
                getProperty(key).setValue(value);
            } else {
                RuleActionProperty property = new RuleActionProperty();
                property.setKey(key);
                property.setValue(value);
                mProperties.add(property);
            }
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    // ------------------------------------------------------------------------
    //  GETTERS / SETTERS
    // ------------------------------------------------------------------------

    /**
     * Generates the list of properties what this plugin needs to be persisted.
     *
     * @return the list of all the properties needed.
     */
    public List<RuleActionProperty> getProperties() {
        return mProperties;
    }
}
