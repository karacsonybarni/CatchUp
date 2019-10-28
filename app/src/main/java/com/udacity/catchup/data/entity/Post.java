package com.udacity.catchup.data.entity;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.Arrays;

@Root(name = "entry", strict = false)
public class Post implements Serializable {

    @Element(name = "id")
    private String id;

    @Element(name = "author")
    private Author author;

    @Element(name = "title")
    private String title;

    @Element(name = "content")
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        return "{" + TextUtils.join(", ", Arrays.asList(id, author, title, content)) + "}";
    }
}
