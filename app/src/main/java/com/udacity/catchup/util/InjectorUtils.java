package com.udacity.catchup.util;

import android.content.Context;

import androidx.annotation.Nullable;

import com.udacity.catchup.BuildConfig;
import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.database.Database;
import com.udacity.catchup.data.network.RedditNetworkDataSource;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InjectorUtils {

    public static Repository getRepository(Context context) {
        return getRepositoryWithApplicationContext(context.getApplicationContext());
    }

    private static Repository getRepositoryWithApplicationContext(Context applicationContext) {
        if (BuildConfig.DEBUG && (!isApplicationContext(applicationContext))) {
            throw new WrongContextException();
        }
        Database database = Database.getInstance(applicationContext);
        RedditNetworkDataSource networkDataSource =
                RedditNetworkDataSource.getInstance(applicationContext);
        Executor diskIO = Executors.newSingleThreadExecutor();
        return Repository.getInstance(database, networkDataSource, diskIO);
    }

    private static boolean isApplicationContext(Context context) {
        return context == context.getApplicationContext();
    }

    private static class WrongContextException extends RuntimeException {

        @Nullable
        @Override
        public String getMessage() {
            return "The provided context is not the application context.";
        }
    }
}
