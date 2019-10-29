package com.udacity.catchup.util;

import android.content.Context;

import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.database.Database;
import com.udacity.catchup.data.network.RedditNetworkDataSource;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InjectorUtils {

    public static Repository getRepository(Context context) {
        Database database = Database.getInstance(context);
        RedditNetworkDataSource networkDataSource = RedditNetworkDataSource.getInstance(context);
        Executor diskIO = Executors.newSingleThreadExecutor();
        return Repository.getInstance(database, networkDataSource, diskIO);
    }
}
