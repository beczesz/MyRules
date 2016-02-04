package com.exarlabs.android.myrules.ui.actions;

import java.util.Set;

import javax.inject.Inject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.action.Action;
import com.exarlabs.android.myrules.business.rule.action.ActionManager;
import com.exarlabs.android.myrules.business.rule.action.ActionPluginManager;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.RuleComponentDetailsFragment;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;
import com.exarlabs.android.myrules.ui.util.ui.spinner.SpinnerItemViewHolder;

import butterknife.Bind;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Displays the details of a action
 * Created by becze on 1/21/2016.
 */
public class ActionDetailsFragment extends RuleComponentDetailsFragment implements AdapterView.OnItemSelectedListener {


    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------
    private class ActionPluginAdapter extends ArrayAdapter<Action.Type> {

        private final int mLayout;

        public ActionPluginAdapter(Context context, int layout) {
            super(context, layout);
            mLayout = layout;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SpinnerItemViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(mLayout, null);
                viewHolder = new SpinnerItemViewHolder(convertView);
                convertView.setTag(viewHolder);
            }

            viewHolder = (SpinnerItemViewHolder) convertView.getTag();

            Action.Type item = getItem(position);
            viewHolder.mItemText.setText(getResources().getText(item.getTitleResId()));

            return convertView;
        }
    }


    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String KEY_ACTION_ID = "ACTION_ID";

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    /**
     * @param actionID
     *
     * @return new instance of ActionDetailsFragment
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

    @Bind (R.id.editText_action_name)
    public EditText mActionName;

    @Bind (R.id.spinner_select_action)
    public MaterialSpinner mActionTypeSpinner;

    @Bind (R.id.action_plugin_fragment_container)
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
    private boolean isInitialized;

    private ActionPluginAdapter mSpinnerAdapter;
    private Action.Type mLastSelecedType;

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

        // get out the action id or type
        mActionId = getArguments().containsKey(KEY_ACTION_ID) ? (long) getArguments().get(KEY_ACTION_ID) : -1;

        // Get the action if we have a valid id
        if (mActionId != -1) {
            mRuleAction = mActionManager.loadAction(mActionId);
            mRuleAction.rebuild();
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
    public void onResume() {
        super.onResume();
        initActionBar();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!isInitialized) {
            isInitialized = true;

            mSpinnerAdapter = new ActionPluginAdapter(getActivity(), R.layout.spinner_item);
            mSpinnerAdapter.addAll(Action.Type.values());
            mActionTypeSpinner.setAdapter(mSpinnerAdapter);
            mActionTypeSpinner.setOnItemSelectedListener(this);


            // In edit mode: init the name field and the select the corresponding item in spinner
            if (mRuleAction.isAttached()) {

                int type = mRuleAction.getType();
                // Set the action title
                //@formatter:off
                String actionName = !TextUtils.isEmpty(
                                mRuleAction.getActionName()) ?
                                mRuleAction.getActionName() :
                                getResources().getString(mActionPluginManager.getFromActionTypeCode(type).getTitleResId());
                mActionName.setText(actionName);
                //@formatter:on

                int position = mSpinnerAdapter.getPosition(mActionPluginManager.getFromActionTypeCode(mRuleAction.getType()));
                mActionTypeSpinner.setSelection(position);
            } else {
                // TODO for some reason here it is not working but at the conditions yes
                // mActionTypeSpinner.setHint(R.string.select_an_action);
            }

        }
    }

    /**
     * Initializes the action bar with the corresponding text
     */
    private void initActionBar(){
        if (mRuleAction.isAttached()) {
            initActionBarWithBackButton(getString(R.string.action_edit));

        } else {
            initActionBarWithBackButton(getString(R.string.action_new));
        }
    }

    /**
     * Clicked on Save button
     */
    @Override
    @OnClick (R.id.button_save)
    protected void saveComponent() {
        super.saveComponent();
    }

    /**
     * Validate the fields.
     *
     * @return true if the fields are valid.
     */
    @Override
    protected boolean validateComponent() {

        // Check the title
        if (TextUtils.isEmpty(mActionName.getText().toString().trim())) {
            mActionName.setError(getActivity().getString(R.string.error_mandatory_field));
            return false;
        }

        // Check if the user has selected a field.
        if (mLastSelecedType == null) {
            mActionTypeSpinner.setError(R.string.error_mandatory_field);
            return false;
        }

        return true;
    }


    @Override
    protected Set<String> getRequiredPermissions() {
        // Get the array of permissions.
        return mActionManager.getPermissions(mRuleAction);
    }


    @Override
    protected void onComponentReadyToSave() {
        mRuleAction.setActionName(mActionName.getText().toString().trim());
        mActionPluginFragment.saveChanges();
        mActionManager.saveAction(mRuleAction);
        goBack();
    }


    /**
     * Clicked on Cancel button
     */
    @OnClick (R.id.button_cancel)
    public void cancelNewAction() {
        // we reset everything what we have set on the object
        mActionManager.refresh(mRuleAction);
        goBack();
    }

    /**
     * Hides the keyboard, and navigates back
     */
    private void goBack() {
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
        if (position != -1) {
            mLastSelecedType = ((Action.Type) mActionTypeSpinner.getSelectedItem());
            mActionTypeSpinner.setHint(null);
            inflateLayout();
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
    private void inflateLayout() {
        int actionType = mLastSelecedType == null ? Action.Type.ARITHMETRIC_ACTION_FIBONACCI.getType() : mLastSelecedType.getType();
        mActionPluginFragment = mActionPluginManager.createNewPluginFragmentInstance(actionType);
        mRuleAction.setType(actionType);
        mRuleAction.rebuild();
        mActionPluginFragment.init(mRuleAction);

        // add the fragment to the container.
        getChildFragmentManager().beginTransaction().replace(R.id.action_plugin_fragment_container, mActionPluginFragment).commit();
    }
    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
