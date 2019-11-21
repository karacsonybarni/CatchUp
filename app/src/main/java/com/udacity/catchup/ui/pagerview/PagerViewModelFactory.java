package com.udacity.catchup.ui.pagerview;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.udacity.catchup.data.Repository;

class PagerViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Repository repository;

    PagerViewModelFactory(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new PagerActivityViewModel(repository);
    }
}
