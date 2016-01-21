package com.android.internal.telephony;

/**
 * The implementation of this interface does the android and the methods will be discovered in runtime with reflection
 * It is necessary to reject a call.
 *
 * This interface must be kept in THIS package!
 *
 * Created by atiyka on 2016.01.20..
 */
public interface ITelephony {
    boolean endCall();

    void answerRingingCall();

    void silenceRinger();
}
