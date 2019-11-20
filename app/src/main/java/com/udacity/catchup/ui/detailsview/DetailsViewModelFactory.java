package com.udacity.catchup.ui.detailsview;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.udacity.catchup.data.Repository;

class DetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Repository repository;
    private String id;

    DetailsViewModelFactory(Repository repository, String id) {
        this.repository = repository;
        this.id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailsActivityViewModel(repository, id);
    }
}
