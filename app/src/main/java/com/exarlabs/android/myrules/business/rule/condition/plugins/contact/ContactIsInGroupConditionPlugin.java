package com.exarlabs.android.myrules.business.rule.condition.plugins.contact;

import java.util.HashSet;
import java.util.Set;

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


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public boolean evaluate(Event event) {
        return true;
    }

    @Override
    public Set<String> getRequiredPermissions() {
        HashSet<String> permissions = new HashSet<>();
        permissions.add(android.Manifest.permission.READ_CONTACTS);
        return permissions;
    }
    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
