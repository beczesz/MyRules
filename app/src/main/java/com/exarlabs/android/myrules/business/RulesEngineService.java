package com.exarlabs.android.myrules.business;

import javax.inject.Inject;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.Pair;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.Rule;
import com.exarlabs.android.myrules.business.rule.RuleManager;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.business.rule.event.RuleEventManager;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.model.dao.RuleRecord;
import com.exarlabs.android.myrules.ui.MainActivity;
import com.exarlabs.android.myrules.ui.R;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Service class which runs in background waiting for rule events and handling them.
 * Created by becze on 1/11/2016.
 */
public class RulesEngineService extends Service {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    /**
     * Class for clients to access. Because we know this service always runs in
     * the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public RulesEngineService getService() {
            return RulesEngineService.this;
        }
    }

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = RulesEngineService.class.getSimpleName();


    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to clearFilter it.
    private static int NOTIFICATION = 12312;


    private static boolean isRunning = false;

    public static boolean isRunning() {
        return isRunning;
    }


    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------


    @Inject
    public RuleManager mRuleManager;

    @Inject
    public RuleEventManager mRuleEventManager;

    // This is the object that receives interactions from clients. See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public RulesEngineService() {
        DaggerManager.component().inject(this);
    }


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public void onCreate() {
        super.onCreate();
        // Init the rules engine
        init();
        showNotification();
        // mark the services that it is running
        isRunning = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        hideNotification();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pIntent = PendingIntent.getActivity(this, NOTIFICATION, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher).setContentTitle(getString(R.string.app_name)).setOngoing(true).setContentText(
                        getString(R.string.rules_engine_running)).setContentIntent(pIntent);

        // Send the notification.
        startForeground(NOTIFICATION, mBuilder.build());
    }

    private void hideNotification() {
        stopForeground(true);
    }

    private void init() {

        //@formatter:off
        mRuleEventManager.getEventObservable()
                        .filter(event -> isRunning())
                        .map(event -> {
                            // Log the events and prepare a bundle with the list of rules which are responding to
                            // the given event
                            logEventDispatched(event);
                            return new Pair<>(event, mRuleManager.getRules(event.getType(), Rule.RuleState.STATE_ACTIVE));
                        })
                        .flatMap(pair -> Observable.from(pair.second).map(record -> new Pair<>(pair.first, (RuleRecord) record)))
                        .filter(eventRulePair -> {
                            // Evaluate each rules and
                            boolean result = eventRulePair.second.evaluate(eventRulePair.first);
                            logEventEvaluated(eventRulePair.first, eventRulePair.second, result);
                            return  result;
                        })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(throwable -> throwable.printStackTrace())
                        .subscribe(eventRulePair -> eventRulePair.second.run(eventRulePair.first));
        //@formatter:on
    }


    /**
     * Log the rule evaluation for the given event.
     *
     * @param event
     * @param ruleRecord
     * @param result
     */
    private void logEventEvaluated(Event event, RuleRecord ruleRecord, boolean result) {
        Log.w(TAG, " --> Rule " + ruleRecord + " by event: " + event + " is evaluated to " + result);
        Log.w("BSZ", "Subscribed on thread: " + Thread.currentThread().getName());
    }

    /**
     * Log a new event dispatch
     *
     * @param event
     */
    private void logEventDispatched(Event event) {
        Log.w(TAG, " --> New " + event + " dispatched.");
    }

    /**
     * Log the ruleaction execution for the given rule dispatched by the event.
     *
     * @param event
     * @param ruleRecord
     * @param ruleAction
     */
    private void logActionRun(Event event, RuleRecord ruleRecord, RuleAction ruleAction) {
        Log.w(TAG, "--> Action: " + ruleAction + " is executed for " + " rule " + ruleRecord + " triggered by Event: " + event + "\n");
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
