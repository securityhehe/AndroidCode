package com.octopus.test.rcadapter.diff;

/**
 * Created by octopus on 2019/01/13.
 */

public class DefaultDiffCallback implements RCDiffUtil.Callback {
    @Override
    public boolean areItemsTheSame(Object oldItem, Object newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    public boolean areContentsTheSame(Object oldItem, Object newItem) {
        return true;
    }
}
