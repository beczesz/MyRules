package com.exarlabs.android.myrules.business.rule;

/**
 * Defines a key-value pair property for a component.
 * A list of these property is associated then with the component to define a specific behaviour.
 * These properties can be saved an retrieved later on.
 * <p>
 * Created by becze on 2/1/2016.
 */
public abstract class RuleComponentProperty {

    /**
     * @return the key of this property
     */
    public abstract String getKey();

    /**
     * Sets the key
     *
     * @param key
     */
    public abstract void setKey(String key);

    /**
     * @return the value of this property
     */
    public abstract String getValue();

    /**
     * Sets the value
     *
     * @param value
     */
    public abstract void setValue(String value);


}
