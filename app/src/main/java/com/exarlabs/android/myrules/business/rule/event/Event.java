package com.exarlabs.android.myrules.business.rule.event;

import com.exarlabs.android.myrules.business.rule.RuleComponent;

/**
 * Created by becze on 12/18/2015.
 */
public interface Event extends RuleComponent{

    /**
     * Rule event types
     */
    class Type {
        public static final int RULE_EVENT_NUMBER = 1000;

        public static final int RULE_EVENT_SMS = 2000;
        public static final int RULE_EVENT_CALL = 3000;
    }
}
