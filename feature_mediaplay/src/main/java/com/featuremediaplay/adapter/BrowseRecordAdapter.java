package com.featuremediaplay.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.featuremediaplay.databinding.ItemBrowseRecordBinding;
import com.libase.base.list.BaseAdapter;
import com.libase.db.UserLookRecord;


import java.util.HashMap;
import java.util.List;

public class BrowseRecordAdapter extends BaseAdapter<UserLookRecord, BrowseRecordAdapter.BrowseRecordViewHolder> {

    private List<UserLookRecord> mList;
    private boolean mIsShowCheckBox;
    private ItemBrowseRecordCallback mCallback;
    private HashMap<UserLookRecord, Boolean> selectedData;

    /// 记录被选中的浏览记录

    @Override
    public void setDatas(List<UserLookRecord> data) {
        mList = data;
        notifyDataSetChanged();
    }

    public void setCallback(ItemBrowseRecordCallback mCallback) {
        this.mCallback = mCallback;
    }

    public void setIsShowCheckBox(boolean IsShowCheckBox) {
        this.mIsShowCheckBox = IsShowCheckBox;
        if (!mIsShowCheckBox) {
            if (selectedData != null && selectedData.size() > 0) {  //mDeleteList如果为null,mDeleteList.size()不会执行就不会空指针
                mCallback.deleteList(selectedData);
                selectedData = null; //删除列表的数据要清空,不干扰下一次删除
                // mIsShowCheckBox = false;     //将所有CheckBox的状态恢复为未选中状态
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BrowseRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemBrowseRecordBinding binding = ItemBrowseRecordBinding.inflate(inflater, parent, false);
        return new BrowseRecordViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BrowseRecordViewHolder holder, int position) {
        UserLookRecord userLookRecord = mList.get(position);
        holder.mBinding.setData(userLookRecord); //设置UI每个浏览记录里的数据
        holder.mBinding.setLabel("# " + userLookRecord.getLabel());
        holder.mBinding.itemBrowseRecordCheckbox.setVisibility(mIsShowCheckBox ? View.VISIBLE : View.GONE);
        if (selectedData == null || selectedData.isEmpty()) {
            holder.mBinding.itemBrowseRecordCheckbox.setChecked(false);  //如果没有选中的记录,说明所有checkBox都没被选中
        } else {   //判断每一条记录的选中状态,在hashMap中有记录则说明被选中了
            if (selectedData.containsKey(userLookRecord)) {
                holder.mBinding.itemBrowseRecordCheckbox.setChecked(true);
            } else {
                holder.mBinding.itemBrowseRecordCheckbox.setChecked(false);
            }
        }
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();

    }

    public class BrowseRecordViewHolder extends RecyclerView.ViewHolder {
        ItemBrowseRecordBinding mBinding;

        public BrowseRecordViewHolder(@NonNull ItemBrowseRecordBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.itemBrowseRecordCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Integer position = getLayoutPosition();
                    if (isChecked) {
                        if (selectedData == null) {
                            selectedData = new HashMap<>();    //说明此时需要HashMap来记录选中状态了
                            selectedData.put(mList.get(position), true);//存下要删除的元素
                        }
                        selectedData.put(mList.get(position), true);//存下要删除的元素
                        // mDeleteList.add(getLayoutPosition());
                    } else {
                        if (selectedData != null) {  //为空说明没有选中的,自然也不用移除
                            selectedData.remove(mList.get(position));  //取消选中则移除
                        }
                        // mDeleteList.remove(position);

                    }
                }
            });
        }
    }

    public interface ItemBrowseRecordCallback {
        void deleteList(HashMap<UserLookRecord, Boolean> selectedData);
    }
}
