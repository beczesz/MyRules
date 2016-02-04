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
import android.widget.Toast;

import com.exarlabs.android.myrules.business.rule.condition.ConditionPluginManager;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.condition.Condition;
import com.exarlabs.android.myrules.business.rule.condition.ConditionManager;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.ui.BaseFragment;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;
import com.exarlabs.android.myrules.ui.util.ui.spinner.SpinnerItemViewHolder;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.Bind;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Displays the details of a condition
 * Created by becze on 1/21/2016.
 */
public class ConditionDetailsFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {

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

    @Bind(R.id.condition_name)
    public EditText mConditionName;

    @Bind(R.id.spinner_select_condition)
    public MaterialSpinner mConditionTypeSpinner;

    @Bind(R.id.condition_plugin_fragment_container)
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
            mConditionTypeSpinner.setAdapter(mSpinnerAdapter);
            mConditionTypeSpinner.setOnItemSelectedListener(this);


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
                mConditionTypeSpinner.setSelection(++position);
            }
        }

    }

    /**
     * Clicked on Save button
     */
    @OnClick(R.id.button_save)
    public void saveNewCondition() {
        if (isValid()) {
            checkPermissions();
        }
    }

    /**
     * Validate the fields.
     * @return true if the fields are valid.
     */
    private boolean isValid() {

        // Check the title
        if (TextUtils.isEmpty(mConditionName.getText().toString().trim())) {
            mConditionName.setError(getActivity().getString(R.string.error_mandatory_field));
            return false;
        }

        // Check if the user has selected a field.
        if (mConditionId == -1 && mConditionTypeSpinner.getSelectedItemPosition() == 0) {
            mConditionTypeSpinner.setError(R.string.error_mandatory_field);
            return false;
        }

        return true;
    }

    /**
     * Checks if all the necessary permission is granted for this rule
     */
    private void checkPermissions() {
        // Get the array of permissions.
        Set<String> permissionsSet = mRuleCondition.getConditionPlugin().getRequiredPermissions();
        String[] permissions = permissionsSet.toArray(new String[permissionsSet.size()]);

        if (permissions.length > 0) {
            //@formatter:off
            // Must be done during an initialization phase like onCreate
            RxPermissions.getInstance(getActivity())
                            .request( permissions)
                            .subscribe(result -> {
                                               if (result) {
                                                  doSave();
                                               } else {
                                                   Toast.makeText(getActivity(), R.string.message_error_permission_denied, Toast.LENGTH_SHORT).show();
                                               }
                                           }
                                       );
            //@formatter:on
        } else {
            doSave();
        }
    }

    private void doSave() {
        mRuleCondition.setConditionName(mConditionName.getText().toString().trim());
        mConditionPluginFragment.saveChanges();
        mConditionManager.saveCondition(mRuleCondition);
        goBack();
    }

    /**
     * Clicked on Cancel button
     */
    @OnClick(R.id.button_cancel)
    public void cancelNewCondition() {
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
        int conditionType = Condition.Type.DEBUG_ALWAYS_TRUE.getType();
        if (position != -1) {
            conditionType = mSpinnerAdapter.getItem(position).getType();
        }
        inflateLayout(conditionType);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Initializes the ConditionPluginFragment with the selected type, and inflates the corresponding layout
     *
     * @param conditionType
     */
    private void inflateLayout(int conditionType) {
        mConditionPluginFragment = mConditionPluginManager.createNewPluginFragmentInstance(conditionType);
        mRuleCondition.setType(conditionType);
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
