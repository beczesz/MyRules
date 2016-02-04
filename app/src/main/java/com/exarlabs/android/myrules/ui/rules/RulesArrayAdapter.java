package com.exarlabs.android.myrules.ui.rules;

import java.util.Collection;

import javax.inject.Inject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.event.EventPluginManager;
import com.exarlabs.android.myrules.model.dao.RuleRecord;
import com.exarlabs.android.myrules.ui.R;
import com.github.aakira.expandablelayout.ExpandableLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * It's a special array adapter for list view items showing the rules
 * Created by atiyka on 2016.01.15..
 */
public class RulesArrayAdapter extends ArrayAdapter<RuleRecord> implements View.OnClickListener {
    // ------------------------------------------------------------------------
    // STATIC CLASSES
    // ------------------------------------------------------------------------

    static class RuleViewHolder
    {
        public RuleViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.header_text)
        public TextView headerText;

        @Bind(R.id.expandable_layout)
        public ExpandableLayout expandableLayout;

        @Bind(R.id.button_edit)
        public TextView editRule;

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

    private OnRuleEditListener mRuleEditListener;

    private Context mContext;
    private Collection<? extends RuleRecord> mRuleRecords;


    @Inject
    public EventPluginManager mEventPluginManager;
    // ------------------------------------------------------------------------
    // INITIALIZERS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------
    public RulesArrayAdapter(Context context) {
        super(context, R.layout.list_view_item_for_main_screens);
        mContext = context;
        DaggerManager.component().inject(this);
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    public void setOnRuleEditListener(OnRuleEditListener listener){
        mRuleEditListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View root;

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            root = inflater.inflate(R.layout.list_view_item_for_main_screens, parent, false);

            root.setTag(new RuleViewHolder(root));
        }
        else
        {
            root = convertView;
        }

        RuleViewHolder holder = (RuleViewHolder) root.getTag();

        RuleRecord rule = (RuleRecord) mRuleRecords.toArray()[position];

        holder.headerText.setText(rule.getRuleName());
        // make the list items expandable
        ((View) holder.headerText.getParent().getParent()).setOnClickListener(this);


        // TODO: T.B.D. - details, text
        String details = "Event: " + getContext().getString(mEventPluginManager.getFromEventCode(rule.getEventCode()).getTitleResId()) +
                        "\nConditions: " + rule.getRuleConditionTree().getChildConditions().size() +
                        "\nActions: " + rule.getRuleActions().size();

        holder.itemDetails.setText(details);

        holder.editRule.setTag(rule.getId());
        holder.editRule.setOnClickListener(this);

        return root;
    }

    @Override
    public void addAll(Collection<? extends RuleRecord> collection) {
        super.addAll(collection);
        mRuleRecords = collection;
    }

    @Override
    public void onClick(View view) {
        // clicked on a RelativeLayout -> expand the list item
        if(view.getClass().equals(RelativeLayout.class)){
            RuleViewHolder holder = new RuleViewHolder(view);
            if (holder.expandableLayout.isExpanded()) holder.expandableLayout.collapse();
            else holder.expandableLayout.expand();

        // else on a button
        } else {
            TextView button = (TextView) view;
            Long id = (Long) button.getTag();
            if(mRuleEditListener != null)
                mRuleEditListener.onRuleEdit(id);
        }
    }



}
