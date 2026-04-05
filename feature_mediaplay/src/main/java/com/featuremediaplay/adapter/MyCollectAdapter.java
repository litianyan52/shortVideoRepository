package com.featuremediaplay.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.video_data.bean.CollectionItem;
import com.featuremediaplay.databinding.ItemMyCollectBinding;
import com.libase.base.list.BaseAdapter;

import java.util.List;

public class MyCollectAdapter extends BaseAdapter<CollectionItem, MyCollectAdapter.MyCollectViewHolder> {

    private List<CollectionItem> mList;
    private ItemClickCallback mCallback;

    public interface ItemClickCallback {
        void ItemIdCallback(int id);
    }

    public void setCallback(ItemClickCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void setDatas(List<CollectionItem> data) {
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
        if (mList != null && position < mList.size()) {
            CollectionItem item = mList.get(position);
            holder.bindData(item, "# " + item.getTag());
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class MyCollectViewHolder extends RecyclerView.ViewHolder {
        private ItemMyCollectBinding mBinding;

        public MyCollectViewHolder(@NonNull ItemMyCollectBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
            binding.getRoot().setOnClickListener(v -> {
                if (mCallback != null && mList != null) {
                    int position = getLayoutPosition();
                    if (position >= 0 && position < mList.size()) {
                        mCallback.ItemIdCallback(mList.get(position).getId());
                    }
                }
            });
        }

        public void bindData(CollectionItem item, String label) {
            mBinding.setData(item);
            mBinding.setLabel(label);
            mBinding.executePendingBindings();
        }
    }
}
