package com.udacity.catchup.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.udacity.catchup.data.entity.Post;
import com.udacity.catchup.data.entity.Subreddit;

import java.util.List;

@Dao
public abstract class RedditDao {

    @Query("SELECT * FROM post ORDER BY `order`")
    public abstract LiveData<List<Post>> getPosts();

    @Query("SELECT * FROM post WHERE id = :id")
    public abstract LiveData<Post> getPost(String id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void updatePosts(List<Post> posts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertPost(Post post);

    @Query("SELECT * FROM subreddit")
    public abstract LiveData<List<Subreddit>> getSubreddits();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertSubreddit(Subreddit subreddit);

    @Delete
    public abstract void removeSubreddit(Subreddit subreddit);
}
