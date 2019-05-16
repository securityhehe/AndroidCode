package com.octopus.test.rcadapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.octopus.test.rcadapter.viewinjector.DefaultViewInjector;
import com.octopus.test.rcadapter.viewinjector.IViewInjector;


/**
 * Created by octopus on 2019/01/13.
 */

public abstract class RCViewHolder<D> extends RecyclerView.ViewHolder {

    private SparseArray<View> viewMap;

    private IViewInjector injector;

    public RCViewHolder(ViewGroup parent, int itemLayoutRes) {
        this(LayoutInflater.from(parent.getContext()).inflate(itemLayoutRes, parent, false));
    }

    public RCViewHolder(View itemView) {
        super(itemView);
        viewMap = new SparseArray<>();
    }

    final void bind(D data) {
        if (injector == null) {
            injector = new DefaultViewInjector(this);
        }
        onBind(data, injector);
    }

    protected abstract void onBind(D data, IViewInjector injector);

    public final <T extends View> T id(int id) {
        View view = viewMap.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            viewMap.put(id, view);
        }
        return (T) view;
    }

}
