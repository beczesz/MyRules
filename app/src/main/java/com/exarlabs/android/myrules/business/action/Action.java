package com.exarlabs.android.myrules.business.action;

import com.exarlabs.android.myrules.business.condition.Condition;
import com.exarlabs.android.myrules.business.event.Event;

/**
 * Base abstract class for all the rule actions. It applies a bridge pattern to decouple
 * the implementation from the Action type.
 *
 * <p>
 *     The actual Action implementation will be done by the plugins, as the condition implementation.
 *     @see {@link Condition}
 * </p>
 * Created by becze on 1/11/2016.
 */
public abstract class Action {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    /**
     * Encapsulates the Condition Types.
     */
    public static class Type {

        // Debug condition types
        public static final int DEBUG_HELLO_WORLD = 100;
    }

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    // condition plugin which encapsulates the implementation.
    private ActionPlugin mActionPlugin;
    private boolean isBuilt;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    /**
     * @return the type of the condition
     */
    public abstract int getType();

    /**
     * Run the action.
     * @param event
     * @return
     */
    public boolean run(Event event) {
        return mActionPlugin.run(event);
    }

    /**
     * Builds the condition if it is not yet built.
     */
    public void build() {
        if (!isBuilt) {

            // First build this condition
            mActionPlugin = ActionPluginFactory.create(getType());
            isBuilt = true;
        }
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------

    public boolean isBuilt() {
        return isBuilt;
    }
}
