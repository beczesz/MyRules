package com.exarlabs.android.myrules.ui.util.ui.spinner;

import android.view.View;
import android.widget.TextView;

import com.exarlabs.android.myrules.ui.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by becze on 2/2/2016.
 */
public class SpinnerItemViewHolder {

    public SpinnerItemViewHolder(View v) {
        ButterKnife.bind(this, v);
    }

    @Bind(R.id.item_text)
    public TextView mItemText;
}
