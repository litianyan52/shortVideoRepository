package com.featuremediaplay.ui.videoList;

import com.example.video_data.bean.ResVideo;
import com.network.bean.ResList;

public interface IVideoListListener {
    void GetSuccess(ResList<ResVideo> data , boolean isFirst);
    void GetFailed(int status_code , String message);
}
