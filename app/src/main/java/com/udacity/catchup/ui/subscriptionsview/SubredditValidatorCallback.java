package com.udacity.catchup.ui.subscriptionsview;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.udacity.catchup.R;
import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.subreddit.Subreddit;
import com.udacity.catchup.data.entity.subreddit.SubredditWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class SubredditValidatorCallback implements Callback<SubredditWrapper> {

    private Repository repository;
    private EditText subredditInput;

    SubredditValidatorCallback(Repository repository, EditText subredditInput) {
        this.repository = repository;
        this.subredditInput = subredditInput;

    }

    @Override
    public void onResponse(Call<SubredditWrapper> call,
                           Response<SubredditWrapper> response) {
        SubredditWrapper responseBody = response.body();
        Subreddit subreddit = responseBody != null ? responseBody.getData() : null;
        //noinspection ConstantConditions
        if (subreddit != null && subreddit.getName() != null) {
            if (!subreddit.isNsfw()) {
                repository.insertSubreddit(subreddit);
                subredditInput.setText("");
            } else {
                showSubredditError(R.string.nsfw_subreddit);
            }
        } else {
            showSubredditError(R.string.subreddit_doesnt_exist);
        }
    }

    private void showSubredditError(int errorTextId) {
        Context context = subredditInput.getContext();
        String errorString = context.getString(errorTextId);
        subredditInput.setError(errorString);
    }

    @Override
    public void onFailure(Call<SubredditWrapper> call, Throwable t) {
        closeKeyboard();
        showConnectionError();
    }

    private void closeKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) subredditInput
                        .getContext()
                        .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(subredditInput.getWindowToken(), 0);
        }
    }

    private void showConnectionError() {
        Toast
                .makeText(
                        subredditInput.getContext(),
                        R.string.no_internet_connection,
                        Toast.LENGTH_LONG)
                .show();
    }
}
