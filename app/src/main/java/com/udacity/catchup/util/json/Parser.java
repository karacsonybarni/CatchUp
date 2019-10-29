package com.udacity.catchup.util.json;

import com.udacity.catchup.data.entity.Author;
import com.udacity.catchup.util.json.descriptor.AuthorDescriptor;

import org.json.JSONException;
import org.json.JSONObject;

public class Parser {

    public static Author parseAuthor(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        Author author = new Author();
        author.setName(jsonObject.getString(AuthorDescriptor.NAME));
        author.setUri(jsonObject.getString(AuthorDescriptor.URI));
        return author;
    }
}
