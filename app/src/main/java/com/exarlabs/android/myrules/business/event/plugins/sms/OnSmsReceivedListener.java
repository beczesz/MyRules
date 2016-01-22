package com.exarlabs.android.myrules.business.event.plugins.sms;

/**
 * Forwards the received SMS to a listener class, which implements this interface
 *
 * Created by atiyka on 2016.01.21..
 */
public interface OnSmsReceivedListener {
    void getSms(String sender, String message);
}
