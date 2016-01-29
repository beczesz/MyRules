package com.exarlabs.android.myrules.business.rule.condition;

import java.util.ArrayList;
import java.util.List;

import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.model.dao.RuleConditionProperty;

/**
 * A ConditionPlugin is an implementation of the behaviour of a RuleCondition.
 * Is is initialized with a list of Propoerties, and this plugin, can modify, delete add new propoerties.
 * Based on these properties is can evaluate itself at any time.
 * <p>
 * Created by becze on 12/18/2015.
 */
public abstract class ConditionPlugin {


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
    // List of properties
    private List<RuleConditionProperty> mProperties;
    private int mPluginType;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public ConditionPlugin(int pluginType) {
        mProperties = new ArrayList<>();
        mPluginType = pluginType;
    }


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    /**
     * Evaluates the condition based on the event wether it is true or false.
     *
     * @param event
     * @return the result of the evaluation. True if the condition holds and false otherwise
     */
    public abstract boolean evaluate(Event event);

    /**
     * Initializes the plugin with the saved properties
     *
     * @param properties
     */
    public void initialize(List<RuleConditionProperty> properties) {
        for (RuleConditionProperty property : properties) {
            saveProperty(property.getKey(), property.getValue());
        }
    }


    /**
     * @param key
     * @return returns true if we have the given propery with the given key
     */
    public boolean hasProperty(String key) {
        if (mProperties != null) {
            for (RuleConditionProperty property : mProperties) {
                if (property.getKey().equals(key)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Retruns the propery with the given key
     *
     * @param key
     * @return
     */
    public RuleConditionProperty getProperty(String key) {

        if (mProperties != null) {
            for (RuleConditionProperty property : mProperties) {
                if (property.getKey().equals(key)) {
                    return property;
                }
            }
        }

        return null;
    }

    /**
     * Adds/updates the property with the given key/value.
     *
     * @param key
     * @param value
     */
    public void saveProperty(String key, String value) {
        if (mProperties != null) {
            if (hasProperty(key)) {
                getProperty(key).setValue(value);
            } else {
                RuleConditionProperty property = new RuleConditionProperty();
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
     * @return the list of all the propoerties needed.
     */
    public List<RuleConditionProperty> getProperties() {
        return mProperties;
    }

    public int getPluginType() {
        return mPluginType;
    }
}
