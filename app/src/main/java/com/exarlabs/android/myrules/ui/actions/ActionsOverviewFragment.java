package com.exarlabs.android.myrules.ui.actions;

import java.util.List;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.exarlabs.android.myrules.business.rule.action.ActionManager;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.devel.DevelManager;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.ui.BaseFragment;
import com.exarlabs.android.myrules.ui.BuildConfig;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;
import com.software.shell.fab.ActionButton;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Provides an overview to the user of all the actions.
 * Created by becze on 11/25/2015.
 */
public class ActionsOverviewFragment extends BaseFragment implements OnActionEditListener {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = ActionsOverviewFragment.class.getSimpleName();

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    /**
     * @return newInstance of SampleFragment
     */
    public static ActionsOverviewFragment newInstance() {
        return new ActionsOverviewFragment();
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    @Bind(R.id.build_info)
    public TextView mDevelInfo;

    @Bind(R.id.listView_actions)
    public ListView mActionsListView;

    @Bind(R.id.fab_add_action)
    public ActionButton mAddActionButton;

    private View mRootView;

    @Inject
    public DevelManager mDevelManager;

    @Inject
    public ActionManager mActionManager;

    @Inject
    public NavigationManager mNavigationManager;

    private ActionsArrayAdapter mAdapter;

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
            mRootView = inflater.inflate(R.layout.actions_overview_layout, null);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initActionBar(true, getString(R.string.my_actions));

        mAdapter = new ActionsArrayAdapter(getContext());
        mAdapter.setOnActionEditListener(this);

        mActionsListView.setAdapter(mAdapter);

        if (BuildConfig.DEBUG) {
            mDevelInfo.setText(mDevelManager.getBuildDescription());
            mDevelInfo.setVisibility(View.VISIBLE);
        }

        mAddActionButton.playShowAnimation();
        updateUI();
    }

    /**
     * Update the list of rules
     */
    private void updateUI() {
        mAdapter.clear();
        List<RuleAction> actions = mActionManager.loadAllActions();
        mAdapter.addAll(actions);
        mAdapter.notifyDataSetChanged();
    }


    @OnClick(R.id.fab_add_action)
    public void showActionDetailsFragment(){
        showActionDetailsFragment((long)-1);
    }

    public void showActionDetailsFragment(Long id){
        mNavigationManager.startActionDetails(id);
    }

    @Override
    public void onActionEdit(Long actionId) {
        showActionDetailsFragment(actionId);
    }
    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
