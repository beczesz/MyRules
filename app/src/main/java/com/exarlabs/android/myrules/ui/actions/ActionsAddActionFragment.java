package com.exarlabs.android.myrules.ui.actions;

import javax.inject.Inject;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.exarlabs.android.myrules.business.action.ActionManager;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.devel.DevelManager;
import com.exarlabs.android.myrules.model.dao.RuleAction;
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
public class ActionsAddActionFragment extends BaseFragment {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = ActionsAddActionFragment.class.getSimpleName();

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    /**
     * @return newInstance of SampleFragment
     */
    public static ActionsAddActionFragment newInstance() {
        return new ActionsAddActionFragment();
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    @Bind(R.id.build_info)
    public TextView mDevelInfo;

    @Bind(R.id.editText_action_name)
    public EditText mActionName;

    @Inject
    public DevelManager mDevelManager;

    @Inject
    public ActionManager mActionManager;

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
            mRootView = inflater.inflate(R.layout.actions_add_action, null);
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

    }

    @OnClick(R.id.button_save)
    public void saveNewAction(){
        RuleAction entity = new RuleAction();
        String name = mActionName.getText().toString();
        entity.setActionName(name);

        mActionManager.insert(entity);
        goBack();
    }

    @OnClick(R.id.button_cancel)
    public void cancelNewAction(){
        goBack();
    }

    private void goBack(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

        mNavigationManager.navigateBack(getActivity());
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}