package com.octopus.test.rcadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.octopus.test.rcadapter.diff.RCDiffUtil;
import com.octopus.test.rcadapter.viewinjector.IViewInjector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by octopus on 2019/01/13.
 */
public class RCAdapterEx extends RCAdapter {

    private final static int TYPE_EX = -100;
    private final static int TYPE_EMPTY = -90;

    private List<RCExViewHolder> headerItems;
    private List<RCExViewHolder> footerItems;


    private View emptyView;


    protected RCAdapterEx() {
        super();
        headerItems = new ArrayList<>();
        footerItems = new ArrayList<>();
    }

    public RCAdapterEx addHeader(View view) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        headerItems.add(new RCExViewHolder(view));
        notifyDataSetChanged();
        return this;
    }

    public RCAdapterEx addFooter(View view) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        footerItems.add(new RCExViewHolder(view));
        notifyDataSetChanged();
        return this;
    }

    public RCAdapterEx addHeader(Context context, int layoutRes) {
        return addHeader(LayoutInflater.from(context).inflate(layoutRes, null));
    }

    public RCAdapterEx addFooter(Context context, int layoutRes) {
        return addFooter(LayoutInflater.from(context).inflate(layoutRes, null));
    }

    public RCAdapterEx enableEmptyView(Context context, int layoutRes) {
        return enableEmptyView(LayoutInflater.from(context).inflate(layoutRes, null));
    }

    public RCAdapterEx enableEmptyView(View emptyView) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        emptyView.setLayoutParams(params);
        this.emptyView = emptyView;
        return this;
    }


    private Object getExItem(int position) {
        if (position < headerItems.size()) {
            return headerItems.get(position);
        } else {
            position -= headerItems.size();
            if (position < footerItems.size()) {
                return footerItems.get(position);
//            } else {
//                return moreLoader;
            }
        }
        return null;
    }


    @Override
    public int getItemViewType(int position) {
        if (emptyView != null && (super.getData() == null || super.getData().size() == 0)) {
            return TYPE_EMPTY;
        }
        if (position < headerItems.size()) {
            return TYPE_EX - position;
        } else {
            position -= headerItems.size();
            if (position < super.getItemCount()) {
                return super.getItemViewType(position);
            } else {
                position -= super.getItemCount();
                if (position < footerItems.size()) {
                    return TYPE_EX - position - headerItems.size();
                } else {
                    return TYPE_EX - headerItems.size() - footerItems.size();
                }
            }
        }
    }

    @Override
    public RCViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RCViewHolder viewHolder;
        if (viewType == TYPE_EMPTY) {
            viewHolder = new RCExViewHolder(emptyView);
        } else if (viewType <= TYPE_EX) {
            Object item = getExItem(TYPE_EX - viewType);
            viewHolder = (RCViewHolder) item;
        } else {
            viewHolder = super.onCreateViewHolder(parent, viewType);
        }
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        if (emptyView != null && (getData() == null || getData().size() == 0)) {
            return 1;
        }
        return footerItems.size() + headerItems.size() + super.getItemCount();
    }

    @Override
    public Object getItem(int position) {
        if (emptyView != null && (getData() == null || getData().size() == 0) && position == 0) {
            return emptyView;
        }
        if (position < headerItems.size()) {
            return headerItems.get(position);
        } else {
            position -= headerItems.size();
            if (position < super.getItemCount()) {
                return super.getItem(position);
            } else {
                position -= super.getItemCount();
                if (position < footerItems.size()) {
                    return footerItems.get(position);
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public RCAdapterEx enableDiff() {
        return (RCAdapterEx) super.enableDiff();
    }

    @Override
    public RCAdapterEx enableDiff(RCDiffUtil.Callback diffCallback) {
        return (RCAdapterEx) super.enableDiff(diffCallback);
    }

    @Override
    public RCAdapterEx registerDefault(int layoutRes, RCInjector RCInjector) {
        return (RCAdapterEx) super.registerDefault(layoutRes, RCInjector);
    }

    @Override
    public <T> RCAdapterEx register(int layoutRes, RCInjector<T> RCInjector) {
        return (RCAdapterEx) super.register(layoutRes, RCInjector);
    }

    @Override
    public RCAdapterEx attachTo(RecyclerView... recyclerViews) {
        return (RCAdapterEx) super.attachTo(recyclerViews);
    }

    private class RCExViewHolder extends RCViewHolder<Object> {


        public RCExViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Object data, IViewInjector injector) {

        }
    }

}
