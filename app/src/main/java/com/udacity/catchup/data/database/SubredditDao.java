package com.udacity.catchup.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.udacity.catchup.data.entity.Subreddit;

import java.util.List;

@Dao
public interface SubredditDao {

    @Query("SELECT * FROM subreddit")
    LiveData<List<Subreddit>> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Subreddit subreddit);

    @Delete
    void remove(Subreddit subreddit);
}
