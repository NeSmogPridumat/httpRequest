package com.dreamteam.httprequest.database.Group;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dreamteam.httprequest.database.Data.GroupDB;

@Database(entities = {GroupDB.class}, version = 1, exportSchema = false)
public abstract class GroupDataBase extends RoomDatabase {
    public abstract GroupDao groupDao();
}
