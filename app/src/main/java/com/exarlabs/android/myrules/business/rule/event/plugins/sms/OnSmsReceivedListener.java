package com.exarlabs.android.myrules.business.rule.event.plugins.sms;

import com.exarlabs.android.myrules.model.contact.Contact;

/**
 * Forwards the received SMS to a listener class, which implements this interface
 * <p>
 * Created by atiyka on 2016.01.21..
 */
public interface OnSmsReceivedListener {

    /**
     * Callback on new incomming SMS
     * @param contact the contact who is sending this SMS
     * @param message the message from the SMS
     */
    void onSMSReceived(Contact contact, String message);
}
