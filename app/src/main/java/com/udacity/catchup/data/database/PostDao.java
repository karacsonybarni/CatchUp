package com.udacity.catchup.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.udacity.catchup.data.entity.Post;

import java.util.List;

@Dao
public abstract class PostDao {

    @Query("SELECT * FROM post ORDER BY `order`")
    public abstract LiveData<List<Post>> getAll();

    @Query("SELECT * FROM post WHERE id = :id")
    public abstract LiveData<Post> get(String id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insert(List<Post> posts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Post post);
}
