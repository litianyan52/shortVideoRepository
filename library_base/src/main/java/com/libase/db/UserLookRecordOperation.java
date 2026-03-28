package com.libase.db;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.room.Room;

import com.libase.base.IRequestCallBack;
import com.network.config.NetworkStatusConfig;

import java.util.List;

public class UserLookRecordOperation {

    private final UserLookRecordDataBase mDataBase;
    private final Handler mMainHandler;

    public UserLookRecordOperation(Context context) {
        mDataBase = Room.databaseBuilder(context,
                UserLookRecordDataBase.class, "UserLookRecord").build();
        mMainHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 创建一个UserLookRecord返回回去,因为数据每次一个个设置很麻烦
     *
     * @param user_id
     * @param video_id
     * @param cover
     * @param label
     * @param title
     * @param duration
     * @param view_time
     * @return
     */
    public UserLookRecord getUserLookRecord(String user_id, int video_id, String cover, String label, String title, String duration, long view_time) {
        synchronized (UserLookRecord.class) {
            UserLookRecord record = new UserLookRecord();
            record.setUser_id(user_id);
            record.setVideo_id(video_id);
            record.setCover(cover);
            record.setLabel(label);
            record.setTitle(title);
            record.setDuration(duration);
            record.setViewTime(view_time);
            return record;
        }
    }

    /**
     * 插入数据
     *
     * @param userLookRecord
     */
    public void Insert(UserLookRecord userLookRecord) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserLookRecord record = mDataBase.getUserLookRecordDao().queryRecord(userLookRecord.getUser_id(),
                        userLookRecord.getVideo_id());
                if (record != null)  //有记录就进行更新
                {
                    mDataBase.getUserLookRecordDao().updateRecord(userLookRecord.getUser_id(),
                            userLookRecord.getVideo_id(), userLookRecord.getViewTime());
                } else {
                    //没有记录再插入
                    mDataBase.getUserLookRecordDao().insertRecord(userLookRecord);
                }
            }
        }).start();

    }

    public void QueryAllRecord(String user_id, IRequestCallBack<List<UserLookRecord>> requestCallBack) {
        new Thread(() ->
        {
            List<UserLookRecord> records = mDataBase.getUserLookRecordDao().getRecords(user_id);
            mMainHandler.post(() -> {
                if (records != null && records.size() > 0) {
                    requestCallBack.RequestSuccess(records);
                } else {
                    requestCallBack.RequestFailed(NetworkStatusConfig.ERROR_STATUS_EMPTY, "没有浏览记录");
                }
            });


        }).start();
    }

    public void Delete(String user_id, List<Integer> video_ids,IRequestCallBack<String> requestCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDataBase.getUserLookRecordDao().deleteRecord(user_id, video_ids);
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        requestCallBack.RequestSuccess("删除完成");
                    }
                });

            }
        }).start();

    }
}
