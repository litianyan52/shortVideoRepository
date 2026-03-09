package com.featurefind.fragment;

import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.video_data.bean.Category;
import com.featurefind.BR;
import com.featurefind.R;
import com.featurefind.adapter.AnchorAdapter;
import com.featurefind.adapter.CategoryAdapter;
import com.featurefind.bean.ResFind;
import com.featurefind.databinding.LayoutFragmentFindBinding;
import com.libase.base.BaseFragment;
import com.libase.config.ArouterPath;
import com.libase.utils.StatusBarUtils;

@Route(path = ArouterPath.Find.FRAGMENT_FIND)
public class FindFragment extends BaseFragment<FindViewModel, LayoutFragmentFindBinding> {

    private CategoryAdapter mCategoryAdapter;
    private AnchorAdapter mAnchorAdapter;

    @Override
    public FindViewModel getViewModel() {
        return new ViewModelProvider(this).get(FindViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_find;
    }

    @Override
    public int getDataBindVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        mViewmodel.requestData();
        initRecyclerView();
        StatusBarUtils.AddStatusHeightToRootView(mDataBinding.getRoot());
        mDataBinding.findSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(ArouterPath.Video.ACTIVITY_SEARCH_FUNCTION).navigation();
            }
        });
    }

    /*
     *初始化发现页面得两个recyclerView
     */
    private void initRecyclerView() {
        mDataBinding.recyclerviewCategory.setLayoutManager(new GridLayoutManager(getContext(), 3));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mDataBinding.recyclerviewTheme.setLayoutManager(layoutManager);
        mCategoryAdapter = new CategoryAdapter();
        /*
         *
         *各个分类点击的数据传递
         */
        mCategoryAdapter.setCategoryCallback(new CategoryAdapter.CategoryCallback() {
            @Override
            public void onItemCategoryClick(Category category) {
                ARouter.getInstance().build(ArouterPath.Find.ACTIVITY_CATEGORY_DETAIL)
                        .withParcelable(ArouterPath.Find.KEY_CATEGORY_DETAIL_DATA, category)
                        .navigation();  //跳转至分类详情页面
            }
        });
        mAnchorAdapter = new AnchorAdapter();
        mDataBinding.recyclerviewCategory.setAdapter(mCategoryAdapter);
        mDataBinding.recyclerviewTheme.setAdapter(mAnchorAdapter);
    }

    @Override
    public void initData() {
        mViewmodel.getResFindData().observe(getViewLifecycleOwner(), new Observer<ResFind>() {  //请求发现页面的数据
            @Override
            public void onChanged(ResFind resFind) {
                mCategoryAdapter.setDatas(resFind.getCategory());
                mAnchorAdapter.setDatas(resFind.getAnchor());
            }
        });

    }
}
