package com.exarlabs.android.myrules.business.condition;

import java.util.List;

import com.exarlabs.android.myrules.business.event.Event;

/**
 * It is a rule condition abstraction which with a bridge pattern it decouples the
 * condition implementation from the type.
 * <p>
 * The concrete condition implementation will specify the type of the condition and it's properties.
 * </p>
 * Created by becze on 12/18/2015.
 */
public abstract class Condition {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    /**
     * Encapsulates the Condition Types.
     */
    public static class Type {

        // Debug condition types
        public static final int DEBUG_ALWAYS_TRUE = 100;
        public static final int DEBUG_ALWAYS_FALSE = 101;
    }

    /**
     * The tyepe of the logical operator
     */
    public static class Operator {
        public static final int AND = 1;
        public static final int OR = 2;

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
    private ConditionPlugin mConditionPlugin;
    private boolean isBuilt;


    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    /**
     * @return the list of child conditions.
     */
    public abstract List<? extends Condition> getChildConditions();

    /**
     * @return the type of the condition
     */
    public abstract int getType();

    /**
     * @return the logical operator type which holds for the children
     */
    public abstract int getOperator();

    /**
     * Builds the condition if it is not yet built.
     */
    public void build() {
        if (!isBuilt) {

            // First build this condition
            mConditionPlugin = ConditionPluginFactory.create(getType());

            List<? extends Condition> childConditions = getChildConditions();
            if (childConditions != null) {
                for (Condition condition : childConditions) {
                    condition.build();
                }
            }

            isBuilt = true;
        }
    }

    /**
     * Evaluates the condition and recursively all of the child-conditions based on the event.
     *
     * @param event
     * @return true if the condition is true otherwise false.
     */
    public boolean evaluate(Event event) {
        // If for some reason it is not yet built then build it
        if (!isBuilt) {
            build();
        }

        // evaluate this condition
        boolean isConditionHolds = mConditionPlugin.evaluate(event);
        int operator = getOperator();

        // Evaluate all of the child conditions recursively if we have children
        List<? extends Condition> childConditions = getChildConditions();
        if (childConditions != null) {
            for (Condition condition : childConditions) {
                switch (operator) {
                    case Operator.AND:
                        isConditionHolds = isConditionHolds && condition.evaluate(event);
                        break;

                    case Operator.OR:
                        isConditionHolds = isConditionHolds || condition.evaluate(event);
                        break;
                }
            }
        }

        return isConditionHolds;
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------

    public boolean isBuilt() {
        return isBuilt;
    }
}
