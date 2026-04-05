package com.featuremediaplay.ui.myCollect;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.video_data.bean.CollectionItem;
import com.featuremediaplay.adapter.MyCollectAdapter;
import com.featuremediaplay.databinding.ActivityMyCollectBinding;
import com.libase.base.BaseActivity;
import com.libase.config.ArouterPath;
import com.libase.utils.StatusBarUtils;

import java.util.List;

@Route(path = ArouterPath.Video.ACTIVITY_MY_COLLECT)
public class MyCollectActivity extends BaseActivity<MyCollectViewModel, ActivityMyCollectBinding> {

    private MyCollectAdapter mAdapter;

    @Override
    public MyCollectViewModel getViewModel() {
        return new MyCollectViewModel();
    }

    @Override
    public int getLayoutId() {
        return com.featuremediaplay.R.layout.activity_my_collect;
    }

    @Override
    public int getViewModelId() {
        return com.featuremediaplay.BR.viewModel;
    }

    @Override
    public void initView() {
        StatusBarUtils.AddStatusHeightToRootView(mdataBinding.getRoot());

        mdataBinding.myCollectBack.setOnClickListener(v -> finish());

        mAdapter = new MyCollectAdapter();
        mdataBinding.myCollectRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mdataBinding.myCollectRecyclerview.setAdapter(mAdapter);

        mAdapter.setCallback(id -> {
            ARouter.getInstance()
                    .build(ArouterPath.Video.ACTIVITY_MEDIA_PLAY)
                    .withInt(ArouterPath.Video.KEY_VIDEO_ID, id)
                    .navigation();
        });
    }

    @Override
    public void initData() {
        mViewModel.getCollectionList().observe(this, new Observer<List<CollectionItem>>() {
            @Override
            public void onChanged(List<CollectionItem> list) {
                mAdapter.setDatas(list);
            }
        });

        mViewModel.loadCollectionList();
    }
}
