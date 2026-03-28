package com.featuremediaplay.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.featuremediaplay.databinding.ItemMyCollectBinding;
import com.libase.base.list.BaseAdapter;
import com.libase.db.UserLookRecord;

import java.util.List;

public class MyCollectAdapter extends BaseAdapter<UserLookRecord, MyCollectAdapter.MyCollectViewHolder> {

    private List<UserLookRecord> mList;

    @Override
    public void setDatas(List<UserLookRecord> data) {
        mList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyCollectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMyCollectBinding binding = ItemMyCollectBinding.inflate(inflater, parent, false);

        return new MyCollectViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCollectViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class MyCollectViewHolder extends RecyclerView.ViewHolder {
        private ItemMyCollectBinding mBinding;

        public MyCollectViewHolder(@NonNull ItemMyCollectBinding binding) {
            super(binding.getRoot());
        }
    }
}
