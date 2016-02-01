package com.exarlabs.android.myrules.business.rule;

/**
 * A buildable component can builds itself based on some properties, which can be provided by a data provider.
 * Usually it is an expensive operation, so the build action should be done on a background thread,
 * and the build state is persisted until the component is rebuilt.
 *
 * @see Rule
 * @see com.exarlabs.android.myrules.business.rule.condition.Condition Condition
 * @see com.exarlabs.android.myrules.business.rule.condition.ConditionPlugin ConditionPlugin
 * @see com.exarlabs.android.myrules.business.rule.action.Action Action
 * @see com.exarlabs.android.myrules.business.rule.action.ActionPlugin ActionPlugin
 * <p>
 * Created by becze on 2/1/2016.
 */
public interface Buildable {

    /**
     * Builds the component and caches it's state into the memory.
     */
    void build();

    /**
     * Destroys it's internal state and rebuilds it.
     */
    void rebuild();

    /**
     * @return true if the component is already built
     */
    boolean isBuilt();


}
