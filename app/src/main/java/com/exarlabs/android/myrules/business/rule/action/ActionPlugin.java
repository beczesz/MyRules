package com.exarlabs.android.myrules.business.rule.action;

import java.util.ArrayList;
import java.util.List;

import com.exarlabs.android.myrules.business.rule.RuleComponentPlugin;
import com.exarlabs.android.myrules.business.rule.RuleComponentProperty;
import com.exarlabs.android.myrules.business.rule.Runnable;
import com.exarlabs.android.myrules.model.dao.RuleActionProperty;

/**
 * A ActionPlugin is an implementation of the behaviour of a RuleAction.
 * Is is initialized with a list of Propoerties, and this plugin, can modify, delete add new propoerties.
 * Based on these properties is can evaluate itself at any time.
 * <p>
 * Created by becze on 12/18/2015.
 */
public abstract class ActionPlugin implements Runnable, RuleComponentPlugin {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = ActionPlugin.class.getSimpleName();

    private int mType;

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

    public ActionPlugin(int type) {
        mProperties = new ArrayList<>();
        this.mType = type;
    }


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public void initialize(List<? extends RuleComponentProperty> properties) {
        for (RuleComponentProperty property : properties) {
            saveProperty(property.getKey(), property.getValue());
        }
    }



    /**
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
     *
     * @param key
     * @return
     */
    public RuleActionProperty getProperty(String key) {

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
     *
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

    public int getType() {
        return mType;
    }
}
