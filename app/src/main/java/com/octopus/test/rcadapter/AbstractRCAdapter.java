package com.octopus.test.rcadapter;

import android.support.v7.widget.RecyclerView;


/**
 * Created by octopus on 22/12/2016.
 */

abstract class AbstractRCAdapter extends RecyclerView.Adapter<RCViewHolder> {

    @Override
    public final void onBindViewHolder(RCViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    protected abstract Object getItem(int position);

}
