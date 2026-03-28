package com.libase.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "user_look_record")
public class UserLookRecord {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "user_id")
    private String user_id;
    @ColumnInfo(name = "video_id")
    private int video_id;
    @ColumnInfo(name = "cover")
    private String cover;
    @ColumnInfo(name = "label")
    private String label;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "duration")
    private String duration;
    @ColumnInfo(name = "view_time")
    private long viewTime;


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public long getViewTime() {
        return viewTime;
    }

    public void setViewTime(long viewTime) {
        this.viewTime = viewTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof UserLookRecord) {
            UserLookRecord record = (UserLookRecord) o;
            if (this.id == record.id && this.user_id.equals(record.user_id) && this.video_id == record.video_id) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user_id, video_id);
    }
}
