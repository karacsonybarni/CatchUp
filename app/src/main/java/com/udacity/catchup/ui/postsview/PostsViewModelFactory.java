package com.udacity.catchup.ui.postsview;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.udacity.catchup.data.Repository;

class PostsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Repository repository;

    PostsViewModelFactory(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new PostsActivityViewModel(repository);
    }
}
