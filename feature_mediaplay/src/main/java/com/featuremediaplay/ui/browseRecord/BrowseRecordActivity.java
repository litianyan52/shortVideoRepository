package com.featuremediaplay.ui.browseRecord;

import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;

import com.featuremediaplay.BR;
import com.featuremediaplay.R;
import com.featuremediaplay.adapter.BrowseRecordAdapter;
import com.featuremediaplay.databinding.ActivityBrowseRecordBinding;
import com.libase.base.BaseActivity;
import com.libase.config.ArouterPath;
import com.libase.db.UserLookRecord;
import com.libase.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Route(path = ArouterPath.Video.ACTIVITY_BROWSE_RECORD)
public class BrowseRecordActivity extends BaseActivity<BrowseRecordViewModel, ActivityBrowseRecordBinding> {


    private BrowseRecordAdapter mAdapter;

    @Override
    public BrowseRecordViewModel getViewModel() {
        return new ViewModelProvider(this).get(BrowseRecordViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_browse_record;
    }

    @Override
    public int getViewModelId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        StatusBarUtils.AddStatusHeightToRootView(mdataBinding.getRoot());
//        mdataBinding.browseRecordEdit.setVisibility(View.GONE);
        mAdapter = new BrowseRecordAdapter();
        mAdapter.setCallback(new BrowseRecordAdapter.ItemBrowseRecordCallback() {
            @Override
            public void deleteList(HashMap<UserLookRecord, Boolean> selectedData) {
                List<Integer> deleteList = new ArrayList<>();
                for (UserLookRecord userLookRecord : selectedData.keySet()) {
                    deleteList.add(userLookRecord.getVideo_id());
                }
                mViewModel.DeleteRecord(deleteList);
            }
        });
        mdataBinding.browseRecordRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mdataBinding.browseRecordRecyclerview.setAdapter(mAdapter);
        mdataBinding.browseRecordEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.getIsShowCheckBox().setValue(!mViewModel.getIsShowCheckBox().getValue());
                if (mViewModel.getIsShowCheckBox().getValue()) {
                    mdataBinding.browseRecordEdit.setText("完成");
                } else {
                    mdataBinding.browseRecordEdit.setText("编辑");
                }

            }
        });

    }

    @Override
    public void initData() {
        mViewModel.QueryAllRecords();
        mViewModel.getRecords().observe(this, new Observer<List<UserLookRecord>>() {
            @Override
            public void onChanged(List<UserLookRecord> userLookRecords) {
                mAdapter.setDatas(userLookRecords);
            }
        });

        mViewModel.getIsShowCheckBox().observe(this, isShowCheckBox ->
        {
            mAdapter.setIsShowCheckBox(isShowCheckBox);
        });
    }
}