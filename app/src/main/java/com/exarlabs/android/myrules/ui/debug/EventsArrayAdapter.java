package com.exarlabs.android.myrules.ui.debug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.ui.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * It's a special array adapter for list view items in the Debug menu
 * Created by atiyka on 2016.01.15..
 */
public class EventsArrayAdapter extends ArrayAdapter<Event.Type> implements View.OnClickListener {
    // ------------------------------------------------------------------------
    // STATIC CLASSES
    // ------------------------------------------------------------------------

    static class ViewHolder {
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.item_text)
        public TextView itemText;

        @Bind(R.id.button_trigger_event)
        public Button triggerEvent;
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

    private OnTriggerEventListener mTriggerEventListener;

    private Context mContext;


    // ------------------------------------------------------------------------
    // INITIALIZERS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------
    public EventsArrayAdapter(Context context) {
        super(context, R.layout.debug_list_view_item);
        mContext = context;
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    public void setOnTriggerEventListener(OnTriggerEventListener listener) {
        mTriggerEventListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View root;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            root = inflater.inflate(R.layout.debug_list_view_item, parent, false);
            root.setTag(new ViewHolder(root));

        } else {
            root = convertView;
        }

        ViewHolder holder = (ViewHolder) root.getTag();

        Event.Type event = getItem(position);

        holder.itemText.setText(event.getTitleResId());
        holder.triggerEvent.setTag(event);
        holder.triggerEvent.setOnClickListener(this);

        return root;
    }

    public void addAllPlugins(Event.Type ... types) {
        addAll(types);
    }

    @Override
    public void onClick(View view) {
        Button button = (Button) view;
        Event.Type event = (Event.Type) button.getTag();
        if (mTriggerEventListener != null) {
            mTriggerEventListener.triggerEvent(event);
        }
    }


}
