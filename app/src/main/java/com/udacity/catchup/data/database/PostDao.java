package com.udacity.catchup.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.udacity.catchup.data.entity.post.Post;

import java.util.List;

@Dao
public interface PostDao {

    @Query("SELECT * FROM post ORDER BY `order`")
    LiveData<List<Post>> getAll();

    @Query("SELECT * FROM post WHERE id = :id")
    LiveData<Post> get(String id);

    @Query("SELECT * FROM post WHERE isSeen = 0 ORDER BY `order` LIMIT 1")
    Post getNextUnseen();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<Post> posts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post post);
}
