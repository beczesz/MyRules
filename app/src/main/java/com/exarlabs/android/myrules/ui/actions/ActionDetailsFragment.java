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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.exarlabs.android.myrules.business.rule.action.ActionManager;
import com.exarlabs.android.myrules.business.rule.action.ActionPlugin;
import com.exarlabs.android.myrules.business.rule.action.ActionPluginManager;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.ui.BaseFragment;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;

/**
 * Displays the detials of a condition
 * Created by becze on 1/21/2016.
 */
public class ActionDetailsFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String KEY_ACTION_ID = "ACTION_ID";

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    /**
     *
     * @param actionID
     * @return new instance of ActionDetailsFragment
     *
     */
    public static ActionDetailsFragment newInstance(long actionID) {
        Bundle args = new Bundle();

        if (actionID != -1) {
            args.putLong(KEY_ACTION_ID, actionID);
        }

        ActionDetailsFragment fragment = new ActionDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    private View mRootView;

    @Bind(R.id.editText_action_name)
    public EditText mActionName;

    @Bind(R.id.spinner_select_action)
    public Spinner mActionTypeSpinner;

    @Bind(R.id.action_plugin_fragment_container)
    public FrameLayout mActionPluginFragmentContainer;

    @Inject
    public ActionManager mActionManager;

    @Inject
    public ActionPluginManager mActionPluginManager;

    @Inject
    public NavigationManager mNavigationManager;

    private RuleAction mRuleAction;
    private Long mActionId;
    private ActionPluginFragment mActionPluginFragment;
    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public ActionDetailsFragment() {
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerManager.component().inject(this);

        // get out the condition id or type
        mActionId = getArguments().containsKey(KEY_ACTION_ID) ? (long) getArguments().get(KEY_ACTION_ID) : -1;

        // Get the action if we have a valid id
        if (mActionId != -1) {
            mRuleAction = mActionManager.loadAction(mActionId);
            mRuleAction.build();
        }

        // if we have no action, we will create a new one
        if (mRuleAction == null) {
            mRuleAction = new RuleAction();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.action_details_layout, null);
        }
        return mRootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initActionBar(true, getString(R.string.my_actions));

        Collection<ActionPlugin> plugins = mActionPluginManager.getPlugins();
        List<CharSequence> pluginsName = new ArrayList<>();

        // only add if adding a new action
        if (mActionId == -1){
            pluginsName.add(getString(R.string.select_an_action));
        }

        // fills the drop down list with the plugins
        Observable.from(plugins)
                        .map(plugin -> plugin.getClass().getSimpleName())
                        .subscribe(name -> pluginsName.add(name));

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, pluginsName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mActionTypeSpinner.setAdapter(adapter);
        mActionTypeSpinner.setOnItemSelectedListener(this);

        // In edit mode: init the name field and the spinner
        int actionType = mRuleAction.getType();
        if(mActionId != -1){
            mActionName.setText(mRuleAction.getActionName());

            int selected = mActionPluginManager.getPositionInMap(actionType);
            mActionTypeSpinner.setSelection(selected);
        }

        inflateLayout(actionType);
    }

    /**
     * Clicked on Save button
     *
     */
    @OnClick(R.id.button_save)
    public void saveNewAction(){
        if(mActionId == -1 && mActionTypeSpinner.getSelectedItemPosition() == 0) {
                // TODO: notify the user that must select an action type
                return;
        }
        String name = mActionName.getText().toString();
        mRuleAction.setActionName(name);
        mActionPluginFragment.saveChanges();
        mActionManager.saveAction(mRuleAction);

        goBack();
    }

    /**
     * Clicked on Cancel button
     *
     */
    @OnClick(R.id.button_cancel)
    public void cancelNewAction(){
        goBack();
    }

    /**
     * Hides the keyboard, and navigates back
     *
     */
    private void goBack(){
        // hide the keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

        mNavigationManager.navigateBack(getActivity());
    }

    /**
     * This method will be called when the selected item changed in the spinner
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // in edit mode doesn't exists the first row (Select an action...)
        if(mActionId == -1) {
            position--;
        }

        if (position >= 0) {
            int actionType = mActionPluginManager.getTypeByPosition(position);

            inflateLayout(actionType);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Initializes the ActionPluginFragment with the selected type, and inflates the corresponding layout
     *
     * @param actionType
     */
    private void inflateLayout(int actionType){
        mActionPluginFragment = ActionPluginFragmentFactory.create(actionType);
        mRuleAction.setType(actionType);
        mActionPluginFragment.init(mRuleAction);

        // add the fragment to the container.
        getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.action_plugin_fragment_container, mActionPluginFragment)
                        .commit();
    }
    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
