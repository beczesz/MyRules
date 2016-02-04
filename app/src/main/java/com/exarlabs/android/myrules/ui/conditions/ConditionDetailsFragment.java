package com.exarlabs.android.myrules.ui.conditions;

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

import com.exarlabs.android.myrules.business.rule.condition.ConditionPluginManager;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.action.Action;
import com.exarlabs.android.myrules.business.rule.condition.Condition;
import com.exarlabs.android.myrules.business.rule.condition.ConditionManager;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.RuleComponentDetailsFragment;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;
import com.exarlabs.android.myrules.ui.util.ui.spinner.SpinnerItemViewHolder;

import butterknife.Bind;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Displays the details of a condition
 * Created by becze on 1/21/2016.
 */
public class ConditionDetailsFragment extends RuleComponentDetailsFragment implements AdapterView.OnItemSelectedListener {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    private class ConditionPluginAdapter extends ArrayAdapter<Condition.Type> {

        private final int mLayout;

        public ConditionPluginAdapter(Context context, int layout) {
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

            Condition.Type item = getItem(position);
            viewHolder.mItemText.setText(getResources().getText(item.getTitleResId()));

            return convertView;
        }
    }

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String KEY_CONDITION_ID = "CONDITION_ID";

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    public static ConditionDetailsFragment newInstance(long conditionID) {
        Bundle args = new Bundle();

        if (conditionID != -1) {
            args.putLong(KEY_CONDITION_ID, conditionID);
        }

        ConditionDetailsFragment fragment = new ConditionDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    private View mRootView;

    @Bind (R.id.condition_name)
    public EditText mConditionName;

    @Bind (R.id.spinner_select_condition)
    public MaterialSpinner mTypeSpinner;

    @Bind (R.id.condition_plugin_fragment_container)
    public FrameLayout mConditionPluginFragmentContainer;

    @Inject
    public ConditionManager mConditionManager;

    @Inject
    public ConditionPluginManager mConditionPluginManager;

    @Inject
    public NavigationManager mNavigationManager;

    private RuleCondition mRuleCondition;
    private Long mConditionId;
    private ConditionPluginFragment mConditionPluginFragment;
    private ConditionPluginAdapter mSpinnerAdapter;
    private boolean isInitialized;
    private Condition.Type mLastSelecedType;
    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public ConditionDetailsFragment() {
    }


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerManager.component().inject(this);

        // get out the condition id or type
        mConditionId = getArguments().containsKey(KEY_CONDITION_ID) ? (long) getArguments().get(KEY_CONDITION_ID) : -1;

        // Get the condition if we have a valid id
        if (mConditionId != -1) {
            mRuleCondition = mConditionManager.loadCondition(mConditionId);
            mRuleCondition.rebuild();
        }

        if (mRuleCondition == null) {
            mRuleCondition = new RuleCondition();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.condition_details_layout, null);
        }
        return mRootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initActionBarWithBackButton(getString(R.string.my_conditions));


        if (!isInitialized) {
            isInitialized = true;
            mSpinnerAdapter = new ConditionPluginAdapter(getActivity(), R.layout.spinner_item);
            mSpinnerAdapter.addAll(Condition.Type.values());
            mTypeSpinner.setAdapter(mSpinnerAdapter);
            mTypeSpinner.setOnItemSelectedListener(this);


            // In edit mode: init the name field and the select the corresponding item in spinner
            if (mRuleCondition.isAttached()) {

                int conditionType = mRuleCondition.getType();

                // Set the condition title
                //@formatter:off
                String conditionName = !TextUtils.isEmpty(
                                mRuleCondition.getConditionName()) ?
                                mRuleCondition.getConditionName() :
                                getResources().getString(mConditionPluginManager.getFromConditionTypeCode(conditionType).getTitleResId());
                mConditionName.setText(conditionName);
                //@formatter:on

                int position = mSpinnerAdapter.getPosition(mConditionPluginManager.getFromConditionTypeCode(mRuleCondition.getType()));
                mTypeSpinner.setSelection(position);
            } else {
                mTypeSpinner.setHint(R.string.select_a_condition);
            }
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
        if (TextUtils.isEmpty(mConditionName.getText().toString().trim())) {
            mConditionName.setError(getActivity().getString(R.string.error_mandatory_field));
            return false;
        }

        // Check if the user has selected a field.
        if (mConditionId == -1 && mTypeSpinner.getSelectedItemPosition() == 0) {
            mTypeSpinner.setError(R.string.error_mandatory_field);
            return false;
        }

        return true;
    }

    @Override
    protected Set<String> getRequiredPermissions() {
        return mRuleCondition.getConditionPlugin().getRequiredPermissions();
    }


    @Override
    protected void onComponentReadyToSave() {
        mRuleCondition.setConditionName(mConditionName.getText().toString().trim());
        mConditionPluginFragment.saveChanges();
        mConditionManager.saveCondition(mRuleCondition);
        goBack();
    }

    /**
     * Clicked on Cancel button
     */
    @OnClick (R.id.button_cancel)
    public void cancelNewCondition() {
        // we reset everything what we have set on the object
        mConditionManager.refresh(mRuleCondition);
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
            mLastSelecedType = ((Condition.Type) mTypeSpinner.getSelectedItem());
            mTypeSpinner.setHint(null);
            inflateLayout();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Initializes the ConditionPluginFragment with the selected type, and inflates the corresponding layout
     *
     * @param conditionType
     */
    private void inflateLayout() {
        int type = mLastSelecedType == null ? Condition.Type.DEBUG_ALWAYS_TRUE.getType() : mLastSelecedType.getType();

        mConditionPluginFragment = mConditionPluginManager.createNewPluginFragmentInstance(type);
        mRuleCondition.setType(type);
        mRuleCondition.rebuild();
        mConditionPluginFragment.init(mRuleCondition);

        // add the fragment to the container.
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.condition_plugin_fragment_container,
                                                                             mConditionPluginFragment).commit();
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
