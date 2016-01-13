package com.exarlabs.android.myrules.ui.rules;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.devel.DevelManager;
import com.exarlabs.android.myrules.ui.BaseFragment;
import com.exarlabs.android.myrules.ui.BuildConfig;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Lists all the rules which are defined by the user.
 * Created by becze on 11/25/2015.
 */
public class RulesAddRuleFragment extends BaseFragment {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = RulesAddRuleFragment.class.getSimpleName();

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    /**
     * @return newInstance of SampleFragment
     */
    public static RulesAddRuleFragment newInstance() {
        return new RulesAddRuleFragment();
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    @Bind(R.id.build_info)
    public TextView mDevelInfo;

    @Bind(R.id.edit_rule_name)
    public EditText mRuleName;

    @Bind(R.id.spinner_events)
    public Spinner mSpinnerEvents;

    @Inject
    public DevelManager mDevelManager;

    @Inject
    public NavigationManager mNavigationManager;

    private View mRootView;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerManager.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.rules_add_rule, null);
        }
        return mRootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (BuildConfig.DEBUG) {
            mDevelInfo.setText(mDevelManager.getBuildDescription());
            mDevelInfo.setVisibility(View.VISIBLE);
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.list_of_events, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerEvents.setAdapter(adapter);
    }

    @OnClick(R.id.button_save)
    public void saveNewRule(){
        // TODO: save the new rule in the database
        
    }

    @OnClick(R.id.button_cancel)
    public void cancelNewRule(){
        mNavigationManager.navigateBack(getActivity());
    }
    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
