package com.featuremediaplay.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.video_data.bean.searchVideoBean.ResSearch;
import com.featuremediaplay.databinding.ItemSearchRecyclerviewBinding;
import com.libase.base.list.BaseAdapter;
import com.libase.config.ArouterPath;

import java.util.List;

public class FindSearchAdapter extends BaseAdapter<ResSearch, FindSearchAdapter.SearchAdapterViewHolder> {

    private List<ResSearch> mList;

    @Override
    public void setDatas(List<ResSearch> data) {
        mList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemSearchRecyclerviewBinding itemSearchRecyclerviewBinding = ItemSearchRecyclerviewBinding.inflate(inflater, parent, false);
        return new SearchAdapterViewHolder(itemSearchRecyclerviewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapterViewHolder holder, int position) {
        ResSearch resSearch = mList.get(position);
        holder.mBind.setData(resSearch);
        String tag = resSearch.getTag();
        holder.mBind.setLabel("# " + tag);  //设计图上有#,所以手动添加一下
        holder.mBind.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class SearchAdapterViewHolder extends RecyclerView.ViewHolder {

        private ItemSearchRecyclerviewBinding mBind;

        public SearchAdapterViewHolder(@NonNull ItemSearchRecyclerviewBinding bind) {
            super(bind.getRoot());
            mBind = bind;
            /**
             * 从搜索结果点击视频可以直接播放
             */
            mBind.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    ResSearch resSearch = mList.get(position);
                    ARouter.getInstance().build(ArouterPath.Video.ACTIVITY_MEDIA_PLAY)
                            .withInt(ArouterPath.Video.KEY_VIDEO_ID,resSearch.getId()).navigation();
                }
            });
        }
    }
}
