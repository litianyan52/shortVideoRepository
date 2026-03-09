package com.featuremediaplay.ui.commend.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.featuremediaplay.R;
import com.example.video_data.bean.ResComment;
import com.featuremediaplay.databinding.LayoutRecyclerCommentItemBinding;
import com.libase.base.list.BaseAdapter;

import java.util.List;

public class CommentAdapter extends BaseAdapter<ResComment, CommentAdapter.CommentViewHolder> {
    private List<ResComment> mList;
    private DeleteCallBack callBack;

    @Override
    public void setDatas(List<ResComment> data) {
        mList = data;
        notifyDataSetChanged();
    }

    public void setCallBack(DeleteCallBack callBack) {
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutRecyclerCommentItemBinding binding = (LayoutRecyclerCommentItemBinding) DataBindingUtil.inflate
                (inflater, R.layout.layout_recycler_comment_item, parent, false);
        return new CommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        ResComment resComment = mList.get(position);
        holder.mBinding.setData(resComment);
    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        private LayoutRecyclerCommentItemBinding mBinding;

        public CommentViewHolder(@NonNull LayoutRecyclerCommentItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ResComment resComment = mList.get(getLayoutPosition());
                    callBack.onCommentLongClick(resComment);
                    return true;
                }
            });
        }
    }

    /**
     * id 评论id
     * nickname 评论的作者
     */
    public interface DeleteCallBack {
        void onCommentLongClick(ResComment comment);
    }
}
