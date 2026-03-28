package com.featuremediaplay.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.video_data.bean.ResVideo;
import com.featuremediaplay.databinding.LayoutVideoListRecyclerviewBinding;
import com.libase.base.list.BaseAdapter;
import java.util.List;

public class VideoListAdapter extends BaseAdapter<ResVideo, VideoListAdapter.VideoViewHolder> {

    private boolean mIsWhite;

    public void setTextStyle(boolean isWhite) {
        mIsWhite = isWhite;
    }

    /**
     * 用于实现item点击事件的回调
     */
    public interface ItemClickCallback
    {
        void ItemIdCallback(int id);
    }
    private ItemClickCallback mCallback;
    private List<ResVideo> mDatas;

    public VideoListAdapter() {

    }

    public void setCallback(ItemClickCallback mCallback) {
        this.mCallback = mCallback;
    }

    public void setMvideoArrayList(List<ResVideo> mvideoArrayList) {
        this.mDatas = mvideoArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutVideoListRecyclerviewBinding binding = LayoutVideoListRecyclerviewBinding.inflate(inflater, parent, false);
        return new VideoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        ResVideo resVideo = mDatas.get(position);
//        holder.bind.title.setText(resVideo.getTitle());
//        holder.bind.author.setText(resVideo.getAuthor());
//        holder.bind.durationTime.setText(resVideo.getTime());
        holder.bind.setResVideo(resVideo);
        if (mIsWhite)
        {
            holder.bind.setIsWhite(mIsWhite);  //设置文字样式为白色
        }
        holder.bind.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public void setDatas(List<ResVideo> data) {
        this.mDatas = data;
        notifyDataSetChanged();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        public final LayoutVideoListRecyclerviewBinding bind;

        public VideoViewHolder(LayoutVideoListRecyclerviewBinding binding) {
            super(binding.getRoot());
            this.bind = binding;
            /**
             * 往VideoListFragment传递点中视频的id
             */
            binding.getRoot().setOnClickListener(v->
            {
                mCallback.ItemIdCallback(mDatas.get(getLayoutPosition()).getId());
            });
        }
    }
}
