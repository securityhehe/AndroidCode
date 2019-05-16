package com.octopus.test.rcadapter.loadmore;

import android.view.View;

/**
 * Created by octopus on 2019/01/13
 */
public abstract class LoadMoreViewCreator {

    private RCMoreLoader loader;

    public void attachLoader(RCMoreLoader loader) {
        this.loader = loader;
    }

    protected void reload() {
        loader.loadMore();
    }

    protected abstract View createLoadingView();

    protected abstract View createNoMoreView();

    protected abstract View createPullToLoadMoreView();

    protected abstract View createErrorView();
}
