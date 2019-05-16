package com.octopus.test.rcadapter.loadmore;

import android.content.Context;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.octopus.test.rcadapter.RCAdapter;

import java.util.List;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

/**
 * Created by octopus on 2019/01/13.
 */

public abstract class RCMoreLoader extends RecyclerView.OnScrollListener {
    private static final int WHAT_LOAD_MORE = 1;

    private RCLoadMoreView mLoadMoreView;
    private boolean mLoading;
    private LoadMoreViewCreator mLoadMoreViewCreator;
    private Context mContext;

    private RCAdapter mRcAdapter;
    private android.os.Handler mEventHandler;

    private Handler mHandler;


    protected RCMoreLoader(Context context, LoadMoreViewCreator creator) {
        this.mContext = context;
        this.mLoadMoreViewCreator = creator;
        this.mLoadMoreViewCreator.attachLoader(this);
        initHandler();
    }

    public RCMoreLoader(Context context) {
        this(context, new SimpleLoadMoreViewCreator(context));
    }

    public void setmRcAdapter(RCAdapter mRcAdapter) {
        this.mRcAdapter = mRcAdapter;
    }

    private void initHandler() {
        mHandler = new Handler();
        HandlerThread eventHandlerThread = new HandlerThread(RCMoreLoader.class.getSimpleName() + ".Thread");
        eventHandlerThread.start();
        mEventHandler = new android.os.Handler(eventHandlerThread.getLooper(), new android.os.Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case WHAT_LOAD_MORE:
                        onLoadMore(mHandler);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    public RCLoadMoreView getmLoadMoreView() {
        if (mLoadMoreView == null) {
            mLoadMoreView = new RCLoadMoreView(mContext, mLoadMoreViewCreator);
        }
        return mLoadMoreView;
    }

    protected abstract void onLoadMore(Handler handler);

    protected abstract boolean hasMore();

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                int last = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                if (NO_POSITION == last) {
                    break;
                }
                if (mRcAdapter.getItem(last) == this && !mLoading) {
                    loadMore();
                }
                break;
            default:
                break;
        }
    }

    void loadMore() {
        if (hasMore()) {
            mLoading = true;
            getmLoadMoreView().visibleLoadingView();
            mEventHandler.removeMessages(WHAT_LOAD_MORE);
            mEventHandler.sendEmptyMessage(WHAT_LOAD_MORE);
        } else {
            reset();
        }
    }

    public void reset() {
        mLoading = false;
        if (hasMore()) {
            getmLoadMoreView().visiblePullToLoadMoreView();
        } else {
            getmLoadMoreView().visibleNoMoreView();
        }
    }

    protected final class Handler {

        Handler() {
        }

        public void loadCompleted(List<?> data) {
            if (data == null) {
                reset();
                return;
            }
            if (mLoading) {
                List currentData = mRcAdapter.getData();
                if (currentData == null) {
                    currentData = data;
                } else {
                    currentData.addAll(data);
                }
                mRcAdapter.updateData(currentData);
            }
        }

        public void error() {
            mLoading = false;
            getmLoadMoreView().visibleErrorView();
        }
    }

}
