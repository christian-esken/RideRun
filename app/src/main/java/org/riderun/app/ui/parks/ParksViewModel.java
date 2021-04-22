package org.riderun.app.ui.parks;

import org.riderun.app.storage.mock.ParkMock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ParksViewModel extends ViewModel {

    private MutableLiveData<ParksData> parksData = new MutableLiveData<>();

    public ParksViewModel() {
        ParkMock parkMock = ParkMock.instance();
        ParksData rdata = new ParksData(parkMock.parks(25));
        parksData.setValue(rdata);
    }

    public LiveData<ParksData> getParksData() {
        return parksData;
    }
}