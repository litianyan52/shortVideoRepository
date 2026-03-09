package com.featurefind.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.featurefind.bean.Anchor;
import com.featurefind.databinding.LayoutRecyclerAnchorBinding;
import com.libase.base.list.BaseAdapter;

import java.util.List;

public class AnchorAdapter extends BaseAdapter<Anchor, AnchorAdapter.AnchorViewHolder> {
    private List<Anchor> mList;

    @Override
    public void setDatas(List<Anchor> data) {
        this.mList = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public AnchorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutRecyclerAnchorBinding anchorBinding = LayoutRecyclerAnchorBinding.inflate(inflater, parent, false);
        return new AnchorViewHolder(anchorBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AnchorViewHolder holder, int position) {
        Anchor anchor = mList.get(position);
        holder.mBinding.setData(anchor);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class AnchorViewHolder extends RecyclerView.ViewHolder {
        LayoutRecyclerAnchorBinding mBinding;

        public AnchorViewHolder(@NonNull LayoutRecyclerAnchorBinding anchorBinding) {
            super(anchorBinding.getRoot());
            mBinding = anchorBinding;
        }
    }
}
