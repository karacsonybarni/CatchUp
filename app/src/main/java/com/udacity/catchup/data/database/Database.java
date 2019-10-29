package com.udacity.catchup.data.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.udacity.catchup.data.entity.Post;

@androidx.room.Database(entities = {Post.class}, version = 1, exportSchema = false)
@androidx.room.TypeConverters(TypeConverters.class)
public abstract class Database extends RoomDatabase{

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "posts";

    private static Database sInstance;

    public static Database getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = getDatabaseBuilder(context).build();
            }
        }
        return sInstance;
    }

    private static RoomDatabase.Builder<Database> getDatabaseBuilder(Context context) {
        return Room
                .databaseBuilder(context.getApplicationContext(), Database.class, DATABASE_NAME)
                .fallbackToDestructiveMigration();
    }

    public abstract PostDao postDao();
}
