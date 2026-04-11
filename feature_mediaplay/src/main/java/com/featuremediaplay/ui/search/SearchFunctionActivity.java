package com.featuremediaplay.ui.search;

import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.video_data.bean.searchVideoBean.ResSearch;
import com.featuremediaplay.BR;
import com.featuremediaplay.R;
import com.featuremediaplay.adapter.FindSearchAdapter;
import com.featuremediaplay.databinding.ActivitySearchFunctionBinding;
import com.libase.base.BaseActivity;
import com.libase.config.ArouterPath;
import com.libase.utils.StatusBarUtils;

import java.util.List;

@Route(path = ArouterPath.Video.ACTIVITY_SEARCH_FUNCTION)
public class SearchFunctionActivity extends BaseActivity<SearchFunctionViewModel, ActivitySearchFunctionBinding> {

    private static final String TAG = "SearchFunctionActivity";

    private FindSearchAdapter mAdapter;

    @Override
    public SearchFunctionViewModel getViewModel() {
        return new ViewModelProvider(this).get(SearchFunctionViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_function;
    }

    @Override
    public int getViewModelId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        StatusBarUtils.AddStatusHeightToRootView(mdataBinding.getRoot());
        mdataBinding.inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Log.d(TAG, "onEditorAction: ");
                    String keyword = mdataBinding.inputSearch.getText().toString().trim();
                    mdataBinding.inputSearch.getText().clear();
                    mViewModel.getSearchResult(keyword);
                    return true;
                }
                return false;
            }
        });
        mdataBinding.searchRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FindSearchAdapter();
        /**
         * 从Adapter中拿到要播放的视频信息
         */
        mAdapter.setCallback(new FindSearchAdapter.FinaSearchAdapter() {
            @Override
            public void navigateToMediaPlay(ResSearch resSearch) {
                ARouter.getInstance().build(ArouterPath.Video.ACTIVITY_MEDIA_PLAY)
                        .withInt(ArouterPath.Video.KEY_VIDEO_ID, resSearch.getId()).navigation();
            }
        });
        mdataBinding.searchRecycler.setAdapter(mAdapter);

    }

    @Override
    public void initData() {
        mViewModel.getData().observe(this, new Observer<List<ResSearch>>() {
            @Override
            public void onChanged(List<ResSearch> resSearches) {
                mAdapter.setDatas(resSearches);
            }
        });
    }
}