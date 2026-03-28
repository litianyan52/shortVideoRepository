package com.featuremediaplay.ui.myCollect;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.featuremediaplay.BR;
import com.featuremediaplay.R;

import com.featuremediaplay.databinding.ActivityMyCollectBinding;
import com.libase.base.BaseActivity;
import com.libase.config.ArouterPath;
import com.libase.utils.StatusBarUtils;

@Route(path = ArouterPath.Video.ACTIVITY_MY_COLLECT)
public class MyCollectActivity extends BaseActivity<MyCollectViewModel, ActivityMyCollectBinding> {


    @Override
    public MyCollectViewModel getViewModel() {
        return new ViewModelProvider(this).get(MyCollectViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_collect;
    }

    @Override
    public int getViewModelId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        StatusBarUtils.AddStatusHeightToRootView(mdataBinding.getRoot());
    }

    @Override
    public void initData() {

    }
}