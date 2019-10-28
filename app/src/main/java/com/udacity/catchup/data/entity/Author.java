package com.udacity.catchup.data.entity;

import androidx.annotation.NonNull;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "author")
public class Author {

    @Element(name = "name")
    private String name;

    @Element(name = "uri")
    private String uri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @NonNull
    @Override
    public String toString() {
        return "{" + name + ", " + uri + "}";
    }
}
