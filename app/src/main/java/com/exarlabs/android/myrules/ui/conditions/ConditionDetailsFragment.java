package com.exarlabs.android.myrules.ui.conditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
import android.widget.Toast;

import com.exarlabs.android.myrules.business.condition.ConditionPluginManager;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.condition.ConditionManager;
import com.exarlabs.android.myrules.business.rule.condition.ConditionPlugin;
import com.exarlabs.android.myrules.business.rx.CallbackSubscriber;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.ui.BaseFragment;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;

/**
 * Displays the details of a condition
 * Created by becze on 1/21/2016.
 */
public class ConditionDetailsFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

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
    public Spinner mConditionTypeSpinner;

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
            mRuleCondition.build();
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

        initActionBar(true, getString(R.string.my_conditions));

        Collection<ConditionPlugin> plugins = mConditionPluginManager.getPlugins();
        List<CharSequence> pluginsName = new ArrayList<>();

        // only add if adding a new condition
        if (mConditionId == -1){
            pluginsName.add(getString(R.string.select_a_condition));
        }

        // fills the drop down list with the plugins
        Observable.from(plugins)
                        .map(plugin -> plugin.getClass().getSimpleName())
                        .subscribe(name -> pluginsName.add(name));

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, pluginsName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mConditionTypeSpinner.setAdapter(adapter);
        mConditionTypeSpinner.setOnItemSelectedListener(this);

        // In edit mode: init the name field and the select the corresponding item in spinner
        int conditionType = mRuleCondition.getType();
        if(mConditionId != -1){
            mConditionName.setText(mRuleCondition.getConditionName());

            int selected = mConditionPluginManager.getPositionInMap(conditionType);
            mConditionTypeSpinner.setSelection(selected);
        }

        inflateLayout(conditionType);

    }

    /**
     * Clicked on Save button
     *
     */
    @OnClick(R.id.button_save)
    public void saveNewCondition(){
        checkPermissions();
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
                            .subscribe(new CallbackSubscriber<Boolean>() {
                                           @Override
                                           public void onResult(Boolean result, Throwable e) {
                                               if (result) {
                                                  doSave();
                                               } else {
                                                   Toast.makeText(getActivity(), R.string.message_error_permission_denied, Toast.LENGTH_SHORT).show();
                                               }
                                           }
                                       });
            //@formatter:on
        } else {
            doSave();
        }
    }

    private void doSave() {
        if(mConditionId == -1 && mConditionTypeSpinner.getSelectedItemPosition() == 0) {
            // TODO: notify the user that must select an condition type
            return;
        }
        String name = mConditionName.getText().toString();
        mRuleCondition.setConditionName(name);
        mConditionPluginFragment.saveChanges();
        mConditionManager.saveCondition(mRuleCondition);

        goBack();
    }

    /**
     * Clicked on Cancel button
     *
     */
    @OnClick(R.id.button_cancel)
    public void cancelNewCondition(){
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
        // in edit mode doesn't exists the first row (Select a condition...)
        if(mConditionId == -1) {
            position--;
        }

        if (position >= 0) {
            int conditionType = mConditionPluginManager.getTypeByPosition(position);

            inflateLayout(conditionType);
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
    private void inflateLayout(int conditionType){
        mConditionPluginFragment = ConditionPluginFragmentFactory.create(conditionType);
        mRuleCondition.setType(conditionType);
        mConditionPluginFragment.init(mRuleCondition);

        // add the fragment to the container.
        getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.condition_plugin_fragment_container, mConditionPluginFragment)
                        .commit();
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
