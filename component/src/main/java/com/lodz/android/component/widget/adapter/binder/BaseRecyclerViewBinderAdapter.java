package com.lodz.android.component.widget.adapter.binder;

import android.content.Context;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerBinder的基类适配器
 * Created by zhouL on 2017/1/17.
 */
public class BaseRecyclerViewBinderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    /** 默认的Binder类型 */
    private static final int DEFAULT_BINDER_TYPE = -1;

    /** RecyclerBinder列表 */
    private List<RecyclerBinder> mBinderList = new ArrayList<>();

    private Context mContext;

    public BaseRecyclerViewBinderAdapter(Context context) {
        this.mContext = context;
    }

    public Context getContext(){
        return mContext;
    }

    @Override
    public int getItemViewType(int position) {
        return getViewTypeByPosition(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerBinder binder = getBinderByViewType(viewType);
        if (binder == null){
            return new DefaultRecyclerBinder(getContext(), DEFAULT_BINDER_TYPE).onCreateViewHolder(parent);
        }
        return binder.onCreateViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecyclerBinder binder = getBinderByViewType(getItemViewType(position));
        if (binder == null){
            return;
        }
        binder.onBindViewHolder(holder, getBinderPosition(position));
    }

    @Override
    public int getItemCount() {
        int sumCount = 0;
        for (RecyclerBinder binder: mBinderList) {
            sumCount += binder.getItemCount();
        }
        return sumCount;
    }

    public void addBinder(RecyclerBinder binder){
        mBinderList.add(binder);
    }

    public void removeBinder(int viewType){
        RecyclerBinder binder = getBinderByViewType(viewType);
        if (binder == null){
            return;
        }
        mBinderList.remove(binder);
    }

    /**
     * 根据界面类型获取对于的Binder
     * @param viewType 界面类型
     */
    private RecyclerBinder getBinderByViewType(int viewType){
        for (RecyclerBinder binder: mBinderList) {
            if (binder.getViewType() == viewType){
                return binder;
            }
        }
        return null;
    }

    /**
     * 根据总位置获取对于的ViewType
     * @param position 总位置
     */
    private int getViewTypeByPosition(int position){
        int size = 0;
        for (RecyclerBinder binder: mBinderList) {
            size += binder.getItemCount();
            if (position < size){
                return binder.getViewType();
            }
        }
        return DEFAULT_BINDER_TYPE;
    }

    /**
     * 根据总position换算出Binder内对于的position
     * @param position 总position
     */
    private int getBinderPosition(int position) {
        int currentSize = 0;//当前总数
        int lastSize = 0;//上一次总数
        for (RecyclerBinder binder: mBinderList) {
            currentSize += binder.getItemCount();
            if (position < currentSize){
                return position - lastSize;//当前Binder的相对position
            }
            lastSize = currentSize;
        }
        return 0;
    }

    public void clearBinder(){
        mBinderList.clear();
    }
}
