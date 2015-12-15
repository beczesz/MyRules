package com.exarlabs.android.myrules.business.dagger;

import com.exarlabs.android.myrules.MyRulesApplication;
import com.exarlabs.android.myrules.ui.BaseActivity;
import com.exarlabs.android.myrules.ui.MainActivity;
import com.exarlabs.android.myrules.ui.SampleFragment;
import com.exarlabs.android.myrules.ui.rules.RulesOverviewFragment;

/**
 * Here are listed all the loations where injection is needed.
 * Created by becze on 9/17/2015.
 */
public interface DaggerComponentGraph {


    void inject(MyRulesApplication app);

    void inject(BaseActivity baseActivity);

    void inject(SampleFragment sampleFragment);

    void inject(RulesOverviewFragment rulesOverviewFragment);

    void inject(MainActivity baseActivity);
}
