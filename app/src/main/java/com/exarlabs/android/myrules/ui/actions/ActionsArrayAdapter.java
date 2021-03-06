package com.exarlabs.android.myrules.ui.actions;

import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.model.dao.RuleActionProperty;
import com.exarlabs.android.myrules.ui.R;
import com.github.aakira.expandablelayout.ExpandableLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * It's a special array adapter for list view items showing the actions
 * Created by atiyka on 2016.01.15..
 */
public class ActionsArrayAdapter extends ArrayAdapter<RuleAction> implements View.OnClickListener {
    // ------------------------------------------------------------------------
    // STATIC CLASSES
    // ------------------------------------------------------------------------

    static class ActionHolder
    {
        public ActionHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.header_text)
        public TextView headerText;

        @Bind(R.id.expandable_layout)
        public ExpandableLayout expandableLayout;

        @Bind(R.id.button_edit)
        public TextView editAction;

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

    private OnActionEditListener mActionEditListener;

    private Context mContext;
    private Collection<? extends RuleAction> mRuleActions;


    // ------------------------------------------------------------------------
    // INITIALIZERS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------
    public ActionsArrayAdapter(Context context) {
        super(context, R.layout.list_view_item_for_main_screens);
        mContext = context;
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    public void setOnActionEditListener(OnActionEditListener listener){
        mActionEditListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View root;

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            root = inflater.inflate(R.layout.list_view_item_for_main_screens, parent, false);

            root.setTag(new ActionHolder(root));
        }
        else
        {
            root = convertView;
        }

        ActionHolder holder = (ActionHolder) root.getTag();

        RuleAction action = (RuleAction) mRuleActions.toArray()[position];

        holder.headerText.setText(action.getActionName());
        // make the list items expandable
        ((View) holder.headerText.getParent().getParent()).setOnClickListener(this);

        // TODO: T.B.D. - details, text
        String details = "Type No.: " + action.getType() + "\nClass: " + action.getActionPlugin().getClass().getSimpleName();
        List<RuleActionProperty> properties = action.getProperties();
        for (RuleActionProperty property : properties) {
            details += "\n" + property.getKey() + " = " + property.getValue();
        }

        holder.itemDetails.setText(details);

        holder.editAction.setTag(action.getId());
        holder.editAction.setOnClickListener(this);

        return root;
    }

    @Override
    public void addAll(Collection<? extends RuleAction> collection) {
        super.addAll(collection);
        mRuleActions = collection;
    }

    @Override
    public void onClick(View view) {
        // clicked on a RelativeLayout -> expand the list item
        if(view.getClass().equals(RelativeLayout.class)){
            ActionHolder holder = new ActionHolder(view);
            if (holder.expandableLayout.isExpanded()) holder.expandableLayout.collapse();
            else holder.expandableLayout.expand();

        // else on a button
        } else {
            TextView button = (TextView) view;
            Long id = (Long) button.getTag();
            if(mActionEditListener != null)
                mActionEditListener.onActionEdit(id);
        }
    }



}
