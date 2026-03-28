package com.featuremediaplay.ui.themPlayList;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.video_data.bean.themPlayListBean.ResThemePlayList;
import com.featuremediaplay.BR;
import com.featuremediaplay.R;
import com.featuremediaplay.adapter.ThemePlayListAdapter;
import com.featuremediaplay.databinding.ActivityThemePlayListBinding;
import com.libase.base.BaseActivity;
import com.libase.config.ArouterPath;
import com.libase.utils.StatusBarUtils;

import java.util.List;

@Route(path = ArouterPath.Video.ACTIVITY_THEME_LIST_PLAY)
public class ThemePlayListActivity extends BaseActivity<ThemePlayListViewModel,ActivityThemePlayListBinding> {

    private ThemePlayListAdapter mAdapter;

    @Override
    public ThemePlayListViewModel getViewModel() {
        return new ViewModelProvider(this).get(ThemePlayListViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_theme_play_list;
    }

    @Override
    public int getViewModelId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        StatusBarUtils.AddStatusHeightToRootView(mdataBinding.getRoot());
        /**
         * 先把准备工作做完,拿到数据后再设置进adapter里
         */
        mAdapter = new ThemePlayListAdapter();
       mdataBinding.themeRecy.setLayoutManager(new LinearLayoutManager(ThemePlayListActivity.this));
       mdataBinding.themeRecy.setAdapter(mAdapter);

    }

    @Override
    public void initData() {
        mViewModel.RequestThemeListData(); //请求播单数据
        mViewModel.getResThemePlayData().observe(ThemePlayListActivity.this, new Observer<List<ResThemePlayList>>() {
            @Override
            public void onChanged(List<ResThemePlayList> resThemePlayLists) {
                mAdapter.setDatas(resThemePlayLists);
            }
        });
    }
}