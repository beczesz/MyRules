package com.exarlabs.android.myrules.business.rule.action.plugins.call;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import android.content.Context;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.action.ActionPlugin;
import com.exarlabs.android.myrules.business.rule.event.Event;

/**
 * Action plugin which rejects a call
 * <p>
 * Created by atiyka on 1/20/2016.
 */
public class RejectCallActionPlugin extends ActionPlugin {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = RejectCallActionPlugin.class.getSimpleName();

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    @Inject
    public Context mContext;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public RejectCallActionPlugin() {
        DaggerManager.component().inject(this);
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public boolean run(Event event) {
        rejectCall();
        return true;
    }

    /**
     * rejects a call by finding the ITelephony methods with reflection
     */
    private void rejectCall() {
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = audioManager.getRingerMode();
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

        try {
            Class telephonyManagerClass = Class.forName(telephonyManager.getClass().getName());

            Method iTelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");

            iTelephonyMethod.setAccessible(true);
            ITelephony telephonyService = (ITelephony) iTelephonyMethod.invoke(telephonyManager);

            telephonyService.endCall();

        } catch (ClassNotFoundException e) {
            Log.w(TAG, e);
        } catch (NoSuchMethodException e) {
            Log.w(TAG, e);
        } catch (IllegalAccessException e) {
            Log.w(TAG, e);
        } catch (InvocationTargetException e) {
            Log.w(TAG, e);
        }

        audioManager.setRingerMode(ringerMode);
    }

    @Override
    public Set<String> getRequiredPermissions() {
        // Note this requires the Manifest.permission.MODIFY_PHONE_STATE permission
        // but since this is a system permission which can't be requested we don't have to declare it.
        return new HashSet<>();
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------

}
