package com.featurepiazza.fragment;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.video_data.bean.Category;
import com.featurepiazza.R;
import com.featurepiazza.adapter.piazzaAdapter;
import com.featurepiazza.bean.ResPiazza;
import com.featurepiazza.databinding.LayoutPiazzaFragmentBinding;
import com.libase.base.BaseFragment;
import com.libase.config.ArouterPath;
import com.libase.utils.StatusBarUtils;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

@Route(path = ArouterPath.Piazza.FRAGMENT_PIAZZA)
public class PiazzaFragment extends BaseFragment<PiazzaViewmodel, LayoutPiazzaFragmentBinding> {
    private static final String TAG = "PiazzaFragment";
    private piazzaAdapter mAdapter;

    @Override
    public PiazzaViewmodel getViewModel() {
        return new ViewModelProvider(this).get(PiazzaViewmodel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_piazza_fragment;
    }

    @Override
    public int getDataBindVariableId() {
        return 0;
    }

    @Override
    public void initView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 2;   //第一行两格全占
                } else {
                    return 1;
                }
            }
        });
        mDataBinding.piaSmartRe.listRecyclerview.setLayoutManager(layoutManager);
        mAdapter = new piazzaAdapter();
        mAdapter.setPiazzaCallback(new piazzaAdapter.PiazzaCallback() {
            /**
             * 轮播图的点击跳转
             */
            @Override
            public void onItemBannerClick(Category category) {
                ARouter.getInstance().build(ArouterPath.Find.ACTIVITY_CATEGORY_DETAIL)
                        .withParcelable(ArouterPath.Find.KEY_CATEGORY_DETAIL_DATA, category)
                        .navigation();  //跳转至分类详情页面
            }

            /**
             * 图片浏览的点击跳转
             * @param resPiazzaDetail //所要跳转的页面所需的数据
             */
            @Override
            public void onItemImagesClick(ResPiazza.ResPiazzaDetail resPiazzaDetail) {
                ARouter.getInstance().build(ArouterPath.Piazza.ACTIVITY_IMAGES_SHOW)
                        .withParcelable(ArouterPath.Piazza.KEY_ACTIVITY_IMAGES_SHOW_DATA,resPiazzaDetail)
                        .navigation();  //跳转到图片浏览的页面
              getActivity().overridePendingTransition(R.anim.picture_activity_in,0);//进入动画
            }
        });
        mDataBinding.piaSmartRe.listRecyclerview.setAdapter(mAdapter);
        mDataBinding.piaSmartRe.smartReLayout.setEnableLoadMore(false); //关掉加载
        mDataBinding.piaSmartRe.smartReLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mViewmodel.RequestData();  //上拉刷新
            }
        });
        StatusBarUtils.AddStatusHeightToRootView(mDataBinding.getRoot());
    }

    @Override
    public void initData() {
        Log.d(TAG, "initData: ");
        mViewmodel.RequestData();
        mViewmodel.getmData().observe(getViewLifecycleOwner(), new Observer<List<ResPiazza>>() {
            @Override
            public void onChanged(List<ResPiazza> resPiazzaDetails) {
                if (mDataBinding.piaSmartRe.smartReLayout.isRefreshing()) {
                    mDataBinding.piaSmartRe.smartReLayout.finishRefresh();
                }
                mAdapter.setDatas(resPiazzaDetails);
            }
        });
    }
}
