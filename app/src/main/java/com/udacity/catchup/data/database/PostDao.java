package com.udacity.catchup.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.udacity.catchup.data.entity.Post;

import java.util.List;

@Dao
public abstract class PostDao {

    @Query("SELECT * FROM post")
    public abstract LiveData<List<Post>> getPosts();

    @Transaction
    public void updatePosts(List<Post> posts) {
        deleteAllPosts();
        bulkInsert(posts);
    }

    @Query("DELETE FROM post")
    abstract void deleteAllPosts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void bulkInsert(List<Post> posts);
}