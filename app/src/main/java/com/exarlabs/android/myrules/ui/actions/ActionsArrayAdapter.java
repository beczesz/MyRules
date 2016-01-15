package com.exarlabs.android.myrules.ui.actions;

import java.util.Collection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.ui.R;
import com.github.aakira.expandablelayout.ExpandableLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by atiyka on 2016.01.15..
 */
public class ActionsArrayAdapter extends ArrayAdapter<RuleAction> implements View.OnClickListener {
    // ------------------------------------------------------------------------
    // STATIC CLASSES
    // ------------------------------------------------------------------------

    static class ActionHolder
    {
        private ActionHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.header_text)
        public TextView headerText;

        @Bind(R.id.expandable_layout)
        public ExpandableLayout expandableLayout;

        @Bind(R.id.item_name)
        public TextView itemName;

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

    private Context mContext;
    private Collection<? extends RuleAction> mRuleActions;

    // ------------------------------------------------------------------------
    // INITIALIZERS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------
    public ActionsArrayAdapter(Context context) {
        super(context, R.layout.actions_list_view_item);
        mContext = context;
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View root;

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            root = inflater.inflate(R.layout.actions_list_view_item, parent, false);

            root.setTag(new ActionHolder(root));
        }
        else
        {
            root = convertView;
        }

        ActionHolder holder = (ActionHolder) root.getTag();

        RuleAction action = (RuleAction) mRuleActions.toArray()[position];

        holder.headerText.setText(action.getActionName());
        ((View) holder.headerText.getParent()).setOnClickListener(this);
        holder.itemName.setText(action.getId()+"");

        return root;
    }

    @Override
    public void addAll(Collection<? extends RuleAction> collection) {
        super.addAll(collection);
        mRuleActions = collection;
    }

    @Override
    public void onClick(View view) {
        ActionHolder holder = new ActionHolder(view);
        if (holder.expandableLayout.isExpanded()) holder.expandableLayout.collapse();
        else holder.expandableLayout.expand();
    }

}
