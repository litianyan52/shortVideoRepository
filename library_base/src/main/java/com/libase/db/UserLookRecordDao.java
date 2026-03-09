package com.libase.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserLookRecordDao {
    @Insert
    void insertRecord(UserLookRecord record);

    @Query("SELECT * FROM user_look_record WHERE user_id = :user_id AND video_id = :video_id LIMIT 1")
    UserLookRecord queryRecord(String user_id, int video_id);

    //更新浏览时间
    @Query("UPDATE user_look_record SET view_time =:viewTime WHERE user_id = :user_id AND video_id = :video_id")
    void updateRecord(String user_id, int video_id, long viewTime);

    /**
     * 查出当前用户的所有浏览记录,按浏览时间降序排列,时间越大越靠前表示记录越新
     *
     * @param user_id
     * @return
     */
    @Query("SELECT* FROM user_look_record WHERE user_id = :user_id ORDER BY view_time DESC ")
    List<UserLookRecord> getRecords(String user_id);

    //删除记录,返回删除了几条数据
    @Query("DELETE FROM user_look_record WHERE user_id = :user_id AND video_id IN (:video_ids)")
    int deleteRecord(String user_id, List<Integer> video_ids);
}
