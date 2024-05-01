// author: francis_kikulwe_s2110844
package org.me.gcu.francis_kikulwe_s2110844.ui.forecast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ForecastViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ForecastViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is forecast fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
