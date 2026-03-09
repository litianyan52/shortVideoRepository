package com.libase.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = UserLookRecord.class, version = 1, exportSchema = false)
public abstract class UserLookRecordDataBase extends RoomDatabase {
    public abstract UserLookRecordDao getUserLookRecordDao();
}
