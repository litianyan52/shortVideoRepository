package com.featuremediaplay.ui.videoList;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;

import com.alibaba.android.arouter.launcher.ARouter;
import com.featuremediaplay.adapter.VideoListAdapter;
import com.example.video_data.bean.ResVideo;
import com.libase.base.list.BaseAdapter;
import com.libase.base.list.BaseListFragment;
import com.libase.base.list.BaseListViewmodel;
import com.libase.config.ArouterPath;

@Route(path = ArouterPath.Video.FRAGMENT_VideoList)
public class VideoListFragment extends BaseListFragment<ResVideo> implements VideoListAdapter.ItemClickCallback{

    private static final String TAG = "VideoListFragment";
    @Autowired(name = ArouterPath.Video.VIDEO_LIST_FRAGMENT_TYPE_KEY)
    public int type;  //不能私有化,不然自动注解不了
    @Autowired(name = ArouterPath.Video.VIDEO_STYLE_KEY)
    public boolean isWhite; //视频的文字样式是否为白色
    private VideoListAdapter mVideoListAdapter;

    @Override
    public BaseListViewmodel getViewModel() {
        return new ViewModelProvider(this).get(VideoListViewModel.class);
    }

    @Override
    public int getDataBindVariableId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        VideoListViewModel videoListViewModel = (VideoListViewModel) mViewmodel;
        videoListViewModel.setPageType(type); //指定fragment类型,这个逻辑是独有的,所以不在基类中处理
        super.initData();  //类型要在请求数据之前指定
    }

    @Override
    public BaseAdapter<ResVideo, VideoListAdapter.VideoViewHolder> getAdapter() {  //不知道编译器怎么知道具体类型的


        mVideoListAdapter = new VideoListAdapter();
        mVideoListAdapter.setTextStyle(isWhite); //设置文字样式是否为白色
        mVideoListAdapter.setCallback(this);  //在传入给基类时就把回调传过去
        return mVideoListAdapter;
    }

    /**
     * 返回recyclerview的布局形式
     * @return
     */
    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    /**
     * 接受注册在适配器中回传出来的视屏id,再传给播放页面播放指定视频
     * @param id
     */
    @Override
    public void ItemIdCallback(int id) {
        ARouter.getInstance().build(ArouterPath.Video.ACTIVITY_MEDIA_PLAY)
                .withInt(ArouterPath.Video.KEY_VIDEO_ID,id)
                .navigation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mVideoListAdapter.setCallback(null);//让VideoListAdapter不再持有VideoListFragment
    }

    //    public void setCode(int code) {
//        mViewmodel.setErrorCode(code);
//    }
}


