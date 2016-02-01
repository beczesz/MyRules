package com.exarlabs.android.myrules.business.rule;

/**
 * Represents a component of a rule, which can be Event, Condition and Action.
 * Every rule component has it's specific purpose and behaviour, which helps
 * to define how a Rule is triggered, when should be executed, and how should be
 * executed.
 *
 * @author becze
 * @see Rule
 * @see com.exarlabs.android.myrules.business.rule.event.Event
 * @see com.exarlabs.android.myrules.business.rule.condition.Condition
 * @see com.exarlabs.android.myrules.business.rule.action.Action
 * <p>
 * Created by becze on 1/29/2016.
 */
public interface RuleComponent {

    /**
     * @return the type of the component. Each component has to define it's own type codes.
     */
    int getType();

}
