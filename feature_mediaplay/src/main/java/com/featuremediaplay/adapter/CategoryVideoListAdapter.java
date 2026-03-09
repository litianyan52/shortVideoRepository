package com.featuremediaplay.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.video_data.bean.categoryVideoBean.ResCategoryVideo;
import com.featuremediaplay.databinding.ItemCategoryVideoListBinding;
import com.libase.base.list.BaseAdapter;

import java.util.List;

public class CategoryVideoListAdapter extends BaseAdapter<ResCategoryVideo, CategoryVideoListAdapter.CategoryVideoViewHolder> {
    private List<ResCategoryVideo> mList;
    private CategoryAdapterCallback mCallback;

    public void setCallback(CategoryAdapterCallback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void setDatas(List<ResCategoryVideo> data) {
        mList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCategoryVideoListBinding categoryVideoListBinding = ItemCategoryVideoListBinding.inflate(inflater, parent, false);
        return new CategoryVideoViewHolder(categoryVideoListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryVideoViewHolder holder, int position) {
        ResCategoryVideo data = mList.get(position);
        holder.mBind.setData(data);
        holder.mBind.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class CategoryVideoViewHolder extends RecyclerView.ViewHolder {
        private ItemCategoryVideoListBinding mBind;

        public CategoryVideoViewHolder(@NonNull ItemCategoryVideoListBinding categoryVideoListBinding) {
            super(categoryVideoListBinding.getRoot());
            mBind = categoryVideoListBinding;
            //点赞事件
            mBind.cateVideoLike.setOnClickListener(v -> {
                if (mCallback != null && mList != null) {
                    int position = getLayoutPosition();
                    ResCategoryVideo resCategoryVideo = mList.get(position);
                    mCallback.LikeCallback(resCategoryVideo);   //回传数据
                }

            });
            //收藏事件
            mBind.cateVideoCollect.setOnClickListener(v -> {
                if (mCallback != null && mList != null) {
                    int position = getLayoutPosition();
                    ResCategoryVideo resCategoryVideo = mList.get(position);
                    mCallback.CollectBack(resCategoryVideo);   //回传数据
                }
            });
            //传出视频id
            mBind.cateVideoCover.setOnClickListener(v -> {
                if (mCallback != null && mList != null) {
                    int position = getLayoutPosition();
                    ResCategoryVideo resCategoryVideo = mList.get(position);
                    mCallback.VideoPlayCallback(resCategoryVideo.getId());
                }
            });
        }
    }


    public interface CategoryAdapterCallback {
        //点击事件回调
        void LikeCallback(ResCategoryVideo resCategoryVideo);

        //收藏事件回调
        void CollectBack(ResCategoryVideo resCategoryVideo);

        //评论事件回调
        void VideoPlayCallback(int id);
    }
}
