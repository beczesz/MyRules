package com.exarlabs.android.myrules.ui.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.exarlabs.android.myrules.business.action.ActionManager;
import com.exarlabs.android.myrules.business.action.ActionPlugin;
import com.exarlabs.android.myrules.business.action.ActionPluginManager;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.devel.DevelManager;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.ui.BaseFragment;
import com.exarlabs.android.myrules.ui.BuildConfig;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;

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

    private static final String KEY_RULE_ACTION_ID = "RULE_ACTION_ID";
    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    /**
     * @return newInstance of SampleFragment
     */
    public static ActionsAddActionFragment newInstance(Long id) {
        Bundle args = new Bundle();
        ActionsAddActionFragment fragment = new ActionsAddActionFragment();
        if(id != null) {
            args.putLong(KEY_RULE_ACTION_ID, id);
            fragment.setArguments(args);
        }
        return fragment;
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    @Bind(R.id.build_info)
    public TextView mDevelInfo;

    @Bind(R.id.editText_action_name)
    public EditText mActionName;

    @Bind(R.id.spinner_select_action)
    public Spinner mActionSpinner;

    @Inject
    public DevelManager mDevelManager;

    @Inject
    public ActionManager mActionManager;

    @Inject
    public ActionPluginManager mActionPluginManager;

    @Inject
    public NavigationManager mNavigationManager;

    private View mRootView;
    private Long mId = null;

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
        Bundle args = getArguments();
        if(args != null){
            mId = (Long) getArguments().get(KEY_RULE_ACTION_ID);
        }

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

        initActionBar(true, getString(R.string.my_actions));

        if (BuildConfig.DEBUG) {
            mDevelInfo.setText(mDevelManager.getBuildDescription());
            mDevelInfo.setVisibility(View.VISIBLE);
        }

        Collection<ActionPlugin> plugins = mActionPluginManager.getPlugins();
        List<CharSequence> pluginsName = new ArrayList<>();

        // only add if adding a new action
        if (mId == null){
            pluginsName.add(getString(R.string.select_an_action));
        }

        Observable.from(plugins)
                        .map(plugin -> plugin.getClass().getSimpleName())
                        .subscribe(name -> pluginsName.add(name));

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, pluginsName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mActionSpinner.setAdapter(adapter);

        // In edit mode:
        if(mId != null){
            RuleAction ruleAction = mActionManager.loadAction(mId);
            mActionName.setText(ruleAction.getActionName());

            int type = ruleAction.getType();
            int selected = mActionPluginManager.getPositionInMap(type);

            mActionSpinner.setSelection(selected);
        }
    }

    @OnClick(R.id.button_save)
    public void saveNewAction(){

        // ADD NEW
        if(mId == null) {
            if(mActionSpinner.getSelectedItemPosition() == 0)
                return;

            RuleAction entity = new RuleAction();
            String name = mActionName.getText().toString();
            int actionTypeNo = mActionSpinner.getSelectedItemPosition() - 1;
            int actionType = mActionPluginManager.getTypeByPosition(actionTypeNo);

            entity.setActionName(name);
            entity.setType(actionType);
            mActionManager.insert(entity);

        // IN EDIT MODE
        }else{
            RuleAction entity = mActionManager.loadAction(mId);
            String name = mActionName.getText().toString();
            int actionTypeNo = mActionSpinner.getSelectedItemPosition();
            int actionType = mActionPluginManager.getTypeByPosition(actionTypeNo);

            entity.setActionName(name);
            entity.setType(actionType);
            mActionManager.update(entity);
        }
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
