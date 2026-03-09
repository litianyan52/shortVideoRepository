package com.featuremediaplay.ui.browseRecord;

import com.libase.base.BaseApplication;
import com.libase.base.IRequestCallBack;
import com.libase.bean.ResUserData;
import com.libase.bean.ResUserInfo;
import com.libase.db.UserLookRecord;
import com.libase.db.UserLookRecordOperation;
import com.libase.manager.UserManager;

import java.util.List;

public class BrowseRecordModel {
    public boolean isLogin() {
        return UserManager.getInstance().isLogin();
    }


    /**
     * 插入浏览记录
     *
     * @param video_id
     * @param cover
     * @param label
     * @param title
     * @param duration
     */
    public void InsertData(int video_id, String cover, String label, String title, String duration) {
        String user_id;
        UserLookRecordOperation operation = new UserLookRecordOperation(BaseApplication.getContext());
        if (isLogin()) {
            ResUserData<ResUserInfo> userInfo = UserManager.getInstance().getUserInfo();
            user_id = userInfo.getUser().getId();

        } else {
            user_id = "0"; //没登录id设置为:"0"
        }

        long view_time = System.currentTimeMillis();  //获取当前时间戳
        UserLookRecord record = operation.getUserLookRecord(user_id, video_id, cover, label, title, duration, view_time);
        operation.Insert(record);
    }


    public void QueryBrowseAllRecords(IRequestCallBack<List<UserLookRecord>> requestCallBack) {
        String user_id;
        UserLookRecordOperation operation = new UserLookRecordOperation(BaseApplication.getContext());
        if (isLogin()) {
            ResUserData<ResUserInfo> userInfo = UserManager.getInstance().getUserInfo();
            user_id = userInfo.getUser().getId();

        } else {
            user_id = "0"; //没登录id设置为:"0"
        }

        operation.QueryAllRecord(user_id, new IRequestCallBack<List<UserLookRecord>>() {
            @Override
            public void RequestSuccess(List<UserLookRecord> result) {
                requestCallBack.RequestSuccess(result);
            }

            @Override
            public void RequestFailed(int errorCodeValue, String errorMsg) {
                requestCallBack.RequestFailed(errorCodeValue, errorMsg);
            }
        });
    }

    public void DeleteRecords(List<Integer> deleteList,IRequestCallBack<String> requestCallBack)
    {
        String user_id;
        UserLookRecordOperation operation = new UserLookRecordOperation(BaseApplication.getContext());
        if (isLogin()) {
            ResUserData<ResUserInfo> userInfo = UserManager.getInstance().getUserInfo();
            user_id = userInfo.getUser().getId();

        } else {
            user_id = "0"; //没登录id设置为:"0"
        }


        operation.Delete(user_id, deleteList, new IRequestCallBack<String>() {
            @Override
            public void RequestSuccess(String result) {
                requestCallBack.RequestSuccess(result);
            }

            @Override
            public void RequestFailed(int errorCodeValue, String errorMsg) {

            }
        });
    }
}
