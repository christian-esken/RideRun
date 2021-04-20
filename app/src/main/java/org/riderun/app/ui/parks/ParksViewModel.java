package org.riderun.app.ui.parks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ParksViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ParksViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Park Selector");
    }

    public LiveData<String> getText() {
        return mText;
    }
}