package com.featuremediaplay.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.video_data.bean.themPlayListBean.ResThemePlayList;
import com.featuremediaplay.R;
import com.featuremediaplay.databinding.ItemThemePalyListBinding;
import com.libase.base.list.BaseAdapter;
import com.libase.config.ArouterPath;

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
            View view = mBinding.getRoot();
            /**
             * 点击后跳转至对应页面
             */
            view.findViewById(R.id.entry1_br).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    ResThemePlayList resThemePlayList = mList.get(position);
                    List<ResThemePlayList.ListsBean> lists = resThemePlayList.getLists();
                    int id = lists.get(0).getId();
                    ARouter.getInstance().build(ArouterPath.Video.ACTIVITY_MEDIA_PLAY).
                            withInt(ArouterPath.Video.KEY_VIDEO_ID,id).navigation();
                }
            });


            view.findViewById(R.id.entry2_br).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    ResThemePlayList resThemePlayList = mList.get(position);
                    List<ResThemePlayList.ListsBean> lists = resThemePlayList.getLists();
                    int id = lists.get(1).getId();
                    ARouter.getInstance().build(ArouterPath.Video.ACTIVITY_MEDIA_PLAY).
                            withInt(ArouterPath.Video.KEY_VIDEO_ID,id).navigation();
                }
            });


        }
    }
}
