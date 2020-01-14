package com.udacity.catchup.data.entity.subreddit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Subreddit {

    @SuppressWarnings("NullableProblems")
    @NonNull
    @PrimaryKey
    @SerializedName("display_name")
    private String name;

    @SerializedName("icon_img")
    private String iconUrl;

    @SerializedName("over18")
    private boolean isNsfw;

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @Nullable
    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public boolean isNsfw() {
        return isNsfw;
    }

    public void setNsfw(boolean nsfw) {
        isNsfw = nsfw;
    }
}
