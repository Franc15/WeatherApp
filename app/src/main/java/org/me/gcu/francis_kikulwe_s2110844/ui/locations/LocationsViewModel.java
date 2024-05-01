package org.me.gcu.francis_kikulwe_s2110844.ui.locations;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LocationsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LocationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is locations fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
