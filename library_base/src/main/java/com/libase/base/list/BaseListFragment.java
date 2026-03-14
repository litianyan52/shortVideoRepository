package com.libase.base.list;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.libase.R;
import com.libase.base.BaseFragment;
import com.libase.databinding.LayoutListFragmentBinding;
import com.network.bean.ResList;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

public abstract class BaseListFragment<T> extends BaseFragment<BaseListViewmodel, LayoutListFragmentBinding> {
    private static final String TAG = "BaseListFragment";
    protected BaseAdapter mAdapter;
    private boolean mIsFirst;
    protected List<T> mList;  //适配器的列表数据

    @Override
    public int getLayoutId() {
        return R.layout.layout_list_fragment;
    }

    @Override
    public int getDataBindVariableId() {   //数据的展示是在recyclerview,所以这里不处理数据
        return 0;
    }

    @Override
    public void initView() {
        mDataBinding.listRecyclerview.setLayoutManager(getLayoutManager());
        mAdapter = getAdapter();
        mDataBinding.listRecyclerview.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        mIsFirst = true;
       // loadMoreDatas(mIsFirst); // 加载需要的数据
        mViewmodel.requestListData(mIsFirst);
        //放在第一次请求后面防止加载更多在第一次请求前就触发
        mDataBinding.smartReLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

                mIsFirst = false;
                mViewmodel.requestListData(mIsFirst);
//                loadData(isFirst, type);
             //   loadMoreDatas(mIsFirst);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mIsFirst = true;
//                loadData(isFirst, type)
             //   loadMoreDatas(mIsFirst);
                mViewmodel.requestListData(mIsFirst);
                mViewmodel.getEnableLoadMore().setValue(true);

            }
        });
        mViewmodel.getEnableLoadMore().observe(getViewLifecycleOwner(), new Observer<Boolean>() {//是否可以继续加载
            @Override
            public void onChanged(Boolean enableLoadMore) {
                mDataBinding.smartReLayout.setEnableLoadMore(enableLoadMore);
            }
        });
        mViewmodel.getmData().observe(getViewLifecycleOwner(), new Observer<ResList<T>>() {
            @Override
            public void onChanged(ResList<T> o) {
                if (mDataBinding.smartReLayout.isRefreshing())
                {
                    mDataBinding.smartReLayout.finishRefresh();
                }
                if (mDataBinding.smartReLayout.isLoading())
                {
                    mDataBinding.smartReLayout.finishLoadMore();
                }
                mList = o.getList();
                Log.d(TAG, "onChanged: " + mList);
                mAdapter.setDatas(mList);   //把数据设置进适配器
            }
        });
    }

    /**
     * 这个方法暴露给外部使用,比如每播放一个新的视频下方的视频列表要进行刷新
     */
    public void refreshForExternalUse(){
        mIsFirst = true;
//                loadData(isFirst, type)
        //   loadMoreDatas(mIsFirst);
        mViewmodel.requestListData(mIsFirst);
        mViewmodel.getEnableLoadMore().setValue(true);
    }

    public abstract BaseAdapter getAdapter();  //子类提供Recycler的适配器

    public abstract RecyclerView.LayoutManager getLayoutManager(); //子类提供排版
}
