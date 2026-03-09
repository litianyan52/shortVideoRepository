package com.featuremediaplay.ui.videoList;

import com.example.video_data.bean.ResVideo;
import com.libase.base.list.BaseListViewmodel;

public class VideoListViewModel extends BaseListViewmodel<ResVideo, VideoListModel> {
    private static final String TAG = "VideoListViewModel";
   // private final VideoListModel mvideoListModel;
  //  private MutableLiveData<ResList<ResVideo>> data = new MutableLiveData<>();

 //   private MutableLiveData<String> error = new MutableLiveData<>();
   // private MutableLiveData<Boolean> enableLoadMore = new MutableLiveData<>(true);

//    public MutableLiveData<ResList<ResVideo>> getData() {
//        return data;
//    }

//    public void setData(MutableLiveData<ResList<ResVideo>> data) {
//        this.data = data;
//    }

//    public MutableLiveData<String> getError() {
//        return error;
//    }
//
//    public void setError(MutableLiveData<String> error) {
//        this.error = error;
//    }

//    public void setErrorCode(int errorCode) {
//        this.errorCode.setValue(errorCode);
//    }
//
//
//
//    public MutableLiveData<Boolean> getEnableLoadMore() {
//        return enableLoadMore;
//    }

    public VideoListViewModel() {
       super(new VideoListModel());//
    }
//    public void requestData(boolean isFirst, int type) {
//        mModel.requestDatas(isFirst);
//    }

    public void setPageType(int type) {
        mModel.setPageType(type);
    }
}
