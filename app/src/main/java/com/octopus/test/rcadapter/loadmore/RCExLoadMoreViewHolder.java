package com.octopus.test.rcadapter.loadmore;

import android.view.View;

import com.octopus.test.rcadapter.RCViewHolder;
import com.octopus.test.rcadapter.viewinjector.IViewInjector;

/**
 * Created by octopus on 2019/01/13.
 */
public class RCExLoadMoreViewHolder extends RCViewHolder<RCMoreLoader> {

    public RCExLoadMoreViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(RCMoreLoader data, IViewInjector injector) {
    }
}
