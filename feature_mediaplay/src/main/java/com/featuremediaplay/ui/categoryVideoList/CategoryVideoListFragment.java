package com.featuremediaplay.ui.categoryVideoList;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.video_data.bean.categoryVideoBean.ResCategoryVideo;
import com.featuremediaplay.adapter.CategoryVideoListAdapter;
import com.libase.base.list.BaseAdapter;
import com.libase.base.list.BaseListFragment;
import com.libase.base.list.BaseListViewmodel;
import com.libase.config.ArouterPath;

@Route(path = ArouterPath.Video.FRAGMENT_CATEGORY_VIDEO_LIST)
public class CategoryVideoListFragment extends BaseListFragment<ResCategoryVideo> {
    private static final String TAG = "CategoryVideoListFragme";
    @Autowired(name = ArouterPath.Video.KEY_CHANNEL_ID)
    public int mChannel_id;
    @Autowired(name = ArouterPath.Video.KEY_TYPE)
    public int mType;
    private ResCategoryVideo mResCategoryVideo;

    @Override
    public void initData() {
        CategoryVideoListViewModel categoryVideoListViewModel = (CategoryVideoListViewModel) mViewmodel;
        categoryVideoListViewModel.LoadData(mChannel_id, mType); //为视频请求的参数提前设置数据
        categoryVideoListViewModel.getIsInteract().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                int i = mList.indexOf(mResCategoryVideo);  //该视频在列表中的位置
                mList.set(i, mResCategoryVideo);
                mAdapter.setDatas(mList);   //表明用户进行了点赞或是其他动作,需要更新数据
            }
        });
        super.initData();
    }

    @Override
    public BaseAdapter getAdapter() {
        CategoryVideoListAdapter categoryVideoListAdapter = new CategoryVideoListAdapter();
        categoryVideoListAdapter.setCallback(new CategoryVideoListAdapter.CategoryAdapterCallback() {
            @Override
            public void LikeCallback(ResCategoryVideo resCategoryVideo) {
                mResCategoryVideo = resCategoryVideo;
                CategoryVideoListViewModel categoryVideoListViewModel = (CategoryVideoListViewModel) mViewmodel;
                ((CategoryVideoListViewModel) mViewmodel).onLikeClick(resCategoryVideo);
            }

            @Override
            public void CollectBack(ResCategoryVideo resCategoryVideo) {
                mResCategoryVideo = resCategoryVideo;
                CategoryVideoListViewModel categoryVideoListViewModel = (CategoryVideoListViewModel) mViewmodel;
                ((CategoryVideoListViewModel) mViewmodel).onClickCollect(resCategoryVideo);
            }

            @Override
            public void VideoPlayCallback(int id) {
                ARouter.getInstance().build(ArouterPath.Video.ACTIVITY_MEDIA_PLAY)
                        .withInt(ArouterPath.Video.KEY_VIDEO_ID, id)
                        .navigation();  //跳转到视频播放页
            }
        });
        return categoryVideoListAdapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    @Override
    public BaseListViewmodel getViewModel() {
        return new ViewModelProvider(this).get(CategoryVideoListViewModel.class);
    }
}
