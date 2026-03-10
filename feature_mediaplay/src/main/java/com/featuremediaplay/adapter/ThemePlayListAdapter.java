package com.featuremediaplay.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.video_data.bean.themPlayListBean.ResThemePlayList;
import com.featuremediaplay.databinding.ItemThemePalyListBinding;
import com.libase.base.list.BaseAdapter;

import java.util.List;

public class ThemePlayListAdapter extends BaseAdapter<ResThemePlayList, ThemePlayListAdapter.ThemePlayListViewHolder> {

    private List<ResThemePlayList> mList;
    @Override
    public void setDatas(List data) {
        mList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ThemePlayListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemThemePalyListBinding binding = ItemThemePalyListBinding.inflate(inflater, parent, false);

        return new ThemePlayListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ThemePlayListViewHolder holder, int position) {
        ResThemePlayList resThemePlayList = mList.get(position);
        holder.mBinding.setData(resThemePlayList);
        holder.mBinding.setTag1("#" + resThemePlayList.getLists().get(0).getTag());
        holder.mBinding.setTag2("#" + resThemePlayList.getLists().get(1).getTag());
        holder.mBinding.executePendingBindings();
    }



    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    public class ThemePlayListViewHolder extends RecyclerView.ViewHolder {
        ItemThemePalyListBinding mBinding;

        public ThemePlayListViewHolder(@NonNull ItemThemePalyListBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
