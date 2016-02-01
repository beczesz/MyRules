package com.exarlabs.android.myrules.business.rule;

import java.util.List;
import java.util.Set;

/**
 * A plugin contains business logic specific for the component.
 * This plugin can require permissions to be able to function and these permission must be explicitly
 * stated by the concrete implementation.
 * Created by becze on 2/1/2016.
 */
public interface RuleComponentPlugin {

    /**
     * @return the array of permission needed by this plugin.
     */
    Set<String> getRequiredPermissions();

    /**
     * Initializes the plugin with the list of properties
     *
     * @param properties
     */
    void initialize(List<? extends RuleComponentProperty> properties);

    /**
     * Generates the latest version of the properties out of this plugin..
     *
     * @return the list of new properties.
     */
    List<? extends RuleComponentProperty> getProperties();

}
