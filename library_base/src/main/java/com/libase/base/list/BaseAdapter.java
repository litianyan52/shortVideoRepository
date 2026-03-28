package com.libase.base.list;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
//    protected List<T> mDatas;

    public abstract void setDatas(List<T> data);
//        this.mDatas = data;
//        notifyDataSetChanged();  //未刷新就没数据
//    }
}
