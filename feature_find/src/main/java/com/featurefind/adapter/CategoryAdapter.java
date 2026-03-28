package com.featurefind.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.video_data.bean.Category;
import com.featurefind.databinding.LayoutRecyclerCategoryBinding;
import com.libase.base.list.BaseAdapter;

import java.util.List;

public class CategoryAdapter extends BaseAdapter<Category, CategoryAdapter.CategoryViewHolder> {

    private List<Category> mList;
    private  CategoryCallback mCallback;

    public void setCategoryCallback(CategoryCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void setDatas(List<Category> data) {
        mList = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutRecyclerCategoryBinding categoryBinding = LayoutRecyclerCategoryBinding.inflate(inflater, parent, false);
        return new CategoryViewHolder(categoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = mList.get(position);
        holder.mBinding.setCaData(category);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private LayoutRecyclerCategoryBinding mBinding;

        public CategoryViewHolder(@NonNull LayoutRecyclerCategoryBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.getRoot().setOnClickListener(v->
            {
                mCallback.onItemCategoryClick(mList.get(getLayoutPosition()));//返回该分类的详细数据
            });
        }
    }

    public interface CategoryCallback
    {
        void onItemCategoryClick(Category category);
    }
}
