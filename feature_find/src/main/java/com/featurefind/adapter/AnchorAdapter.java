package com.featurefind.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.featurefind.bean.Anchor;
import com.featurefind.databinding.LayoutRecyclerAnchorBinding;
import com.libase.base.list.BaseAdapter;
import com.libase.config.ArouterPath;

import java.util.List;

public class AnchorAdapter extends BaseAdapter<Anchor, AnchorAdapter.AnchorViewHolder> {
    private List<Anchor> mList;

    private AnchorAdapterCallBack mCallBack;

    @Override
    public void setDatas(List<Anchor> data) {
        this.mList = data;
        notifyDataSetChanged();
    }

    public void setCallBack(AnchorAdapterCallBack mCallBack) {
        this.mCallBack = mCallBack;
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
            /**
             * 点击跳转到主题播单详情页
             */
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // ARouter.getInstance().build(ArouterPath.Video.ACTIVITY_THEME_LIST_PLAY).navigation();
                    mCallBack.navigateToThemeListActivity();
                }
            });
        }
    }


    public interface AnchorAdapterCallBack{
        void navigateToThemeListActivity();
    }
}
