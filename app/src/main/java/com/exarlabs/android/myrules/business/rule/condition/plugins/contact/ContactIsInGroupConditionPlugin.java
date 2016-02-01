package com.exarlabs.android.myrules.business.rule.condition.plugins.contact;

import com.exarlabs.android.myrules.business.rule.condition.ConditionPlugin;
import com.exarlabs.android.myrules.business.rule.event.Event;

/**
 * Checks whether the contact from the event is member of a predefined group.
 * Created by becze on 1/28/2016.
 */
public class ContactIsInGroupConditionPlugin extends ConditionPlugin {

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

    public ContactIsInGroupConditionPlugin(int pluginType) {
        super(pluginType);
    }


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public boolean evaluate(Event event) {
        return true;
    }

    @Override
    public String[] getRequiredPermissions() {
        return new String[] { android.Manifest.permission.READ_CONTACTS };
    }
    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
