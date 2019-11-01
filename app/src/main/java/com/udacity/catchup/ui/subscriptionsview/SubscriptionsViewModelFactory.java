package com.udacity.catchup.ui.subscriptionsview;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.udacity.catchup.data.Repository;

class SubscriptionsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Repository repository;

    SubscriptionsViewModelFactory(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new SubscriptionsActivityViewModel(repository);
    }
}
