package com.udacity.catchup.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Entity
@Root(name = "entry", strict = false)
public class Post {

    @SuppressWarnings("NullableProblems")
    @NonNull
    @PrimaryKey
    @Element(name = "id")
    private String id;

    @Element(name = "author")
    private Author author;

    @Element(name = "title")
    private String title;

    @Element(name = "content")
    private String content;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @NonNull
    @Override
    public String toString() {
        return "{id: " + id +
                ",\nauthor: " + author +
                ",\ntitle: " + title +
                ",\ncontent: " + content + "}";
    }
}
