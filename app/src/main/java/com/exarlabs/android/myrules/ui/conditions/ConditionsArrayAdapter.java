package com.exarlabs.android.myrules.ui.conditions;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exarlabs.android.myrules.business.condition.ConditionPluginManager;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.model.dao.RuleConditionProperty;
import com.exarlabs.android.myrules.ui.R;
import com.github.aakira.expandablelayout.ExpandableLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * It's a special array adapter for list view items showing the conditions in the ConditionOverviewFragment
 * Created by atiyka on 2016.01.28..
 */
public class ConditionsArrayAdapter extends ArrayAdapter<RuleCondition> implements View.OnClickListener {
    // ------------------------------------------------------------------------
    // STATIC CLASSES
    // ------------------------------------------------------------------------

    static class ConditionViewHolder {
        public ConditionViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.header_text)
        public TextView headerText;

        @Bind(R.id.expandable_layout)
        public ExpandableLayout expandableLayout;

        @Bind(R.id.button_edit_condition)
        public TextView editCondition;

        @Bind(R.id.item_details)
        public TextView itemDetails;
    }
    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC INITIALIZERS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    private OnConditionEditListener mConditionEditListener;

    private Context mContext;
    private Collection<? extends RuleCondition> mRuleConditions;


    @Inject
    public ConditionPluginManager mConditionPluginManager;
    // ------------------------------------------------------------------------
    // INITIALIZERS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------
    public ConditionsArrayAdapter(Context context) {
        super(context, R.layout.conditions_list_view_item);

        DaggerManager.component().inject(this);
        mContext = context;
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    public void setOnConditionEditListener(OnConditionEditListener listener) {
        mConditionEditListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View root;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            root = inflater.inflate(R.layout.conditions_list_view_item, parent, false);
            root.setTag(new ConditionViewHolder(root));
        } else {
            root = convertView;
        }

        ConditionViewHolder holder = (ConditionViewHolder) root.getTag();

        RuleCondition condition = (RuleCondition) mRuleConditions.toArray()[position];

        holder.headerText.setText(condition.getConditionName());
        // make the list items expandable
        ((View) holder.headerText.getParent().getParent()).setOnClickListener(this);

        // TODO: T.B.D. - details, text
        String details = "Type No.: " + condition.getType() + "\n" +
                        "Class: " + mConditionPluginManager.getFromConditionTypeCode(condition.getType()).getPluginFragment().getSimpleName();

        List<RuleConditionProperty> properties = condition.getProperties();
        for (RuleConditionProperty property : properties) {
            details += "\n" + property.getKey() + " = " + property.getValue();
        }

        holder.itemDetails.setText(details);
        holder.editCondition.setTag(condition.getId());
        holder.editCondition.setOnClickListener(this);

        return root;
    }

    @Override
    public void addAll(Collection<? extends RuleCondition> collection) {
        super.addAll(collection);
        mRuleConditions = collection;
    }

    @Override
    public void onClick(View view) {
        // clicked on a RelativeLayout -> expand or collapse the list item
        if (view.getClass().equals(RelativeLayout.class)) {
            ConditionViewHolder holder = new ConditionViewHolder(view);
            if (holder.expandableLayout.isExpanded()) holder.expandableLayout.collapse();
            else holder.expandableLayout.expand();

            // else on a button
        } else {
            TextView button = (TextView) view;
            Long id = (Long) button.getTag();
            if (mConditionEditListener != null) mConditionEditListener.onConditionEdit(id);
        }
    }


}
