package com.udacity.catchup.data.database;

import androidx.room.TypeConverter;

import com.udacity.catchup.data.entity.Author;
import com.udacity.catchup.util.json.Parser;
import com.udacity.catchup.util.json.Serializer;

import org.json.JSONException;

@SuppressWarnings("WeakerAccess")
class TypeConverters {

    @TypeConverter
    public static Author toAuthor(String authorString) {
        try {
            return Parser.parseAuthor(authorString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    public static String toAuthorString(Author author) {
        try {
            return Serializer.serializeAuthor(author);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
