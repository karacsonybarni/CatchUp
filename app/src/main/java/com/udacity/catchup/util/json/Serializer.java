package com.udacity.catchup.util.json;

import com.udacity.catchup.data.entity.Author;
import com.udacity.catchup.util.json.descriptor.AuthorDescriptor;

import org.json.JSONException;
import org.json.JSONObject;

public class Serializer {

    public static String serializeAuthor(Author author) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(AuthorDescriptor.NAME, author.getName());
        jsonObject.put(AuthorDescriptor.URI, author.getUri());
        return jsonObject.toString();
    }
}
