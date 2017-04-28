package com.zone.adapter3;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import com.zone.adapter3.base.Header2FooterRcvAdapter;
import com.zone.adapter3.base.IAdapter;
import com.zone.adapter3.bean.Holder;
import com.zone.adapter3.bean.Wrapper;
import com.zone.adapter3.helper.Helper;
import com.zone.adapter3.loadmore.OnScrollRcvListener;
import com.zone.adapter3.loadmore.callback.ILoadMoreDelegates;
import com.zone.adapter3.loadmore.callback.NullLoadMoreDelegates;

import java.util.List;

/**
 * [2017] by Zone
 * 多类型 使用{@link #getItemViewType2(int)}
 */
public class QuickRcvAdapter<T> extends Header2FooterRcvAdapter<T> {

    protected OnItemClickListener onItemClickListener;
    protected OnItemLongClickListener onItemLongClickListener;
    private OnScrollRcvListener listener;
    private ILoadMoreDelegates loadFooterViewHold;
    static QuickConfig quickConfig;

    public QuickRcvAdapter(Context context, List<T> data) {
        super(context, data);
        initQuickConfig();
    }

    private void initQuickConfig() {
        if (quickConfig != null) {
            if (quickConfig.loadMoreDelegates != null) {
                setLoadFooterViewHold(quickConfig.loadMoreDelegates.clone_());
            }
        }
    }

    @Override
    protected Helper createHelper(Holder holder, Context context, Wrapper targetWarpper) {
        initOnItemClickListener(holder);
        return super.createHelper(holder, context, targetWarpper);
    }

    private void initOnItemClickListener(final Holder holder) {
        if (getOnItemClickListener() != null)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOnItemClickListener() != null) {
                        QuickConfig.d("OnItemClick: position" +
                                (holder.getLayoutPosition() - getHeaderViewsCount()));
                        getOnItemClickListener().onItemClick((ViewGroup) holder.itemView.getParent(),
                                v, holder.getLayoutPosition() - getHeaderViewsCount());
                    }
                }
            });
        if (getOnItemLongClickListener() != null)
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (getOnItemLongClickListener() != null) {
                        QuickConfig.d("OnItemLongClick: position" +
                                (holder.getLayoutPosition() - getHeaderViewsCount()));
                        return getOnItemLongClickListener().onItemLongClick((ViewGroup) holder.itemView.getParent(),
                                v, holder.getLayoutPosition() - getHeaderViewsCount());
                    }
                    return false;
                }
            });
    }

    @Override
    public IAdapter setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    @Override
    public IAdapter setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
        return this;
    }

    @Override
    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    @Override
    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    @Override
    public OnScrollRcvListener getOnScrollListener() {
        return listener;
    }

    @Override
    public IAdapter addOnScrollListener(OnScrollRcvListener listener) {
        this.listener = listener;
        mRecyclerView.addOnScrollListener(listener);
        listener.setQuickRcvAdapter(this);
        if (loadFooterViewHold != null)
            listener.setLoadFooterViewHold(loadFooterViewHold);
        return this;
    }

    @Override
    public IAdapter setLoadFooterViewHold(@LayoutRes int layout) {
        loadFooterViewHold = new NullLoadMoreDelegates(layout);
        return this;
    }

    @Override
    public IAdapter setLoadFooterViewHold(ILoadMoreDelegates loadFooterView) {
        loadFooterViewHold = loadFooterView;
        return this;
    }

    @Override
    public IAdapter loadMoreComplete() {
        checkScrollerListener();
        listener.loadMoreFail();
        return this;
    }

    private void checkScrollerListener() {
        if (listener == null)
            throw new IllegalStateException("listener 不能为Null");
    }

    @Override
    public IAdapter loadMoreFail() {
        checkScrollerListener();
        listener.loadMoreFail();
        return this;
    }

    @Override
    public IAdapter end() {
        checkScrollerListener();
        listener.end();
        return this;
    }

    @Override
    public IAdapter clearLoadMoreDelegates() {
        checkScrollerListener();
        listener.clearLoadMoreDelegates();
        return this;
    }
}

