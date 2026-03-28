package com.featuremediaplay.ui.videoList;

import android.util.Log;

import com.featuremediaplay.api.MediaApiService;
import com.featuremediaplay.api.MediaApiServiceProvider;
import com.example.video_data.bean.ResVideo;
import com.libase.base.list.BaseListModel;
import com.libase.config.ArouterPath;
import com.network.ApiCall;
import com.network.bean.ResBase;
import com.network.bean.ResList;

import retrofit2.Call;

public class VideoListModel extends BaseListModel {
    private static final String TAG = "VideoListModel";
//    private IVideoListListener mVideoListListener;

    private int mPageType;

    public VideoListModel() {
  //      Retrofit retrofit = RetrofitProvider.provider();
//        mVideoListListener = iVideoListListener;
    }

//    public void getData(boolean isFirst, int type) { //在最下层才做分开请求
//
//    }


    /**
     * 发起请求
     * isFirst用于判断是否是第一次发送,因为非首次发送就要把前面已经请求到的数据都存起来
     * @param isFirst
     */
    @Override
    public void requestDatas(boolean isFirst) {

        if (isFirst) {
            mPage = 1;
        } else {
            mPage++;
        }
        Call<ResBase<ResList<ResVideo>>> request;
        MediaApiService service = MediaApiServiceProvider.provider();
        if (mPageType == ArouterPath.Video.VIDEO_LIST_FRAGMENT_RECOMMEND) {
            request = service.getRecommend(mPage, mLimit);
        } else {
            request = service.getDaily(mPage, mLimit);
        }

        ApiCall.enqueueList(request, new ApiCall.ApiListCallback<ResVideo>() {  //指定泛型
            @Override
            public void GetLiseSuccess(ResList<ResVideo> result) {
                Log.d(TAG, "requestDatas: " + result);
                iListListener.GetSuccess(result, isFirst);
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                iListListener.GetFailed(errorCode);
            }
        });
    }

    public void setPageType(int type) {
        mPageType = type;
    }
}
