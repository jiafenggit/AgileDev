package com.lodz.android.component.widget.adapter.recycler;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.lodz.android.component.R;
import com.lodz.android.component.widget.adapter.swipe.SwipeMenuLayout;

/**
 * 带侧滑按钮的适配器基类
 * Created by zhouL on 2017/12/18.
 */

public abstract class BaseSwipeRVAdapter<T> extends BaseRecyclerViewAdapter<T>{

    public BaseSwipeRVAdapter(Context context) {
        super(context);
    }

    /** 获取内容布局 */
    @LayoutRes
    protected abstract int getContentLayout();

    /** 初始化右侧布局 */
    @LayoutRes
    protected int getRightLayout(){
        return 0;
    }

    /** 初始化左侧布局 */
    @LayoutRes
    protected int getLeftLayout(){
        return 0;
    }

    /** 获取侧滑的ItemView */
    protected View getSwipeItemView(ViewGroup parent){
        return getLayoutView(parent, R.layout.component_item_swipe_layout);
    }

    /** 配置侧滑菜单的ViewHolder */
    protected void configSwipeViewHolder(SwipeViewHolder holder){
        if (getContentLayout() > 0){
            View contentView = getLayoutView(null, getContentLayout());
            holder.contentLayout.addView(contentView);
        }
        if (getRightLayout() > 0){
            View rightView = getLayoutView(null, getRightLayout());
            holder.rightLayout.addView(rightView);
        }
        if (getLeftLayout() > 0){
            View leftView = getLayoutView(null, getLeftLayout());
            holder.leftLayout.addView(leftView);
        }
        holder.swipeMenuLayout.setSwipeEnable(getRightLayout() > 0 || getLeftLayout() > 0);//没有侧滑菜单禁止滑动
    }

    @Override
    protected void setItemClick(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BaseSwipeRVAdapter.SwipeViewHolder){
            ((BaseSwipeRVAdapter.SwipeViewHolder) holder).contentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position >= 0 && mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(holder, getItem(position), position);
                    }
                }
            });
        }
    }

    @Override
    protected void setItemLongClick(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BaseSwipeRVAdapter.SwipeViewHolder){
            ((BaseSwipeRVAdapter.SwipeViewHolder) holder).contentLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (position >= 0 && mOnItemLongClickListener != null){
                        mOnItemLongClickListener.onItemLongClick(holder, getItem(position), position);
                    }
                    return true;
                }
            });
        }
    }

    protected class SwipeViewHolder extends RecyclerView.ViewHolder{

        /** 侧滑布局 */
        SwipeMenuLayout swipeMenuLayout;
        /** 内容布局 */
        ViewGroup contentLayout;
        /** 右侧布局 */
        ViewGroup rightLayout;
        /** 左侧布局 */
        ViewGroup leftLayout;

        protected SwipeViewHolder(View itemView) {
            super(itemView);
            swipeMenuLayout = itemView.findViewById(R.id.swipe_menu_layout);
            contentLayout = itemView.findViewById(R.id.content_view);
            rightLayout = itemView.findViewById(R.id.right_view);
            leftLayout = itemView.findViewById(R.id.left_view);
        }
    }
}