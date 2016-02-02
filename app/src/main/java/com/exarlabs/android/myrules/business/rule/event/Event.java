package com.exarlabs.android.myrules.business.rule.event;

import javax.inject.Inject;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.RuleComponent;
import com.exarlabs.android.myrules.business.rule.event.plugins.call.CallEvent;
import com.exarlabs.android.myrules.business.rule.event.plugins.call.CallEventHandlerPlugin;
import com.exarlabs.android.myrules.business.rule.event.plugins.math.NumberEvent;
import com.exarlabs.android.myrules.business.rule.event.plugins.math.NumberEventHandlerPlugin;
import com.exarlabs.android.myrules.business.rule.event.plugins.sms.SmsEvent;
import com.exarlabs.android.myrules.business.rule.event.plugins.sms.SmsEventHandlerPlugin;
import com.exarlabs.android.myrules.ui.R;

/**
 * Created by becze on 12/18/2015.
 */
public abstract class Event implements RuleComponent {


    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------


    /**
     * Rule event types
     */
    public enum Type {

        // ------------------------------------------------------------------------
        // MATH EVENTS
        // ------------------------------------------------------------------------

        RULE_EVENT_NUMBER(1000, NumberEvent.class, NumberEventHandlerPlugin.class, R.string.event_title_number_event),
        // ------------------------------------------------------------------------
        // SMS
        // ------------------------------------------------------------------------

        RULE_EVENT_SMS(2000, SmsEvent.class, SmsEventHandlerPlugin.class, R.string.event_title_incomming_sms_event),
        // ------------------------------------------------------------------------
        // CALL
        // ------------------------------------------------------------------------

        RULE_EVENT_CALL(3000, CallEvent.class, CallEventHandlerPlugin.class, R.string.event_title_incomming_call_event);
        // ------------------------------------------------------------------------
        // FIELDS
        // ------------------------------------------------------------------------

        private int mType;
        private Class<? extends Event> mEvent;
        private Class mPlugin;
        private int mTitleResId;


        Type(int type, Class<? extends Event> event, Class<? extends EventHandlerPlugin> plugin, int titleResId) {
            mType = type;
            mEvent = event;
            mPlugin = plugin;
            mTitleResId = titleResId;
        }


        public int getType() {
            return mType;
        }

        public Class<? extends EventHandlerPlugin> getPlugin() {
            return mPlugin;
        }

        public int getTitleResId() {
            return mTitleResId;
        }

        public Class<? extends Event> getEvent() {
            return mEvent;
        }
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

    private final int mType;

    @Inject
    public EventPluginManager mEventPluginManager;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public Event() {
        DaggerManager.component().inject(this);

        // Infer the event type from the plugin manager
        mType = mEventPluginManager.getFromEvent(this.getClass()).getType();
    }


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------


    @Override
    public int getType() {
        return mType;
    }
}
