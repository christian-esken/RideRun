package org.riderun.app.ui.rides;

import org.riderun.app.model.City;
import org.riderun.app.model.Count;
import org.riderun.app.model.Park;
import org.riderun.app.model.Ride;
import org.riderun.app.provider.ProviderFactory;
import org.riderun.app.provider.city.CityProvider;
import org.riderun.app.provider.count.CountProvider;
import org.riderun.app.provider.count.db.PoiKey;
import org.riderun.app.provider.park.ParksProvider;
import org.riderun.app.provider.ride.RidesProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Business logic for the RideFragment.It mainly manages the data: It loads it from the providers,
 * maintains the internal data model (including LiveData) and writes changes back to the providers.
 */
public class RidesViewModel extends ViewModel {
    private final MutableLiveData<RidesData> data = new MutableLiveData<>();
    // The following fields are used to update
    private final CountProvider countProvider = ProviderFactory.countProvider();
    private final ParksProvider parksProvider = ProviderFactory.parksProvider();
    private final CityProvider cityProvider = ProviderFactory.cityProvider();
    private final RidesProvider ridesProvider = ProviderFactory.ridesProvider();

    public RidesViewModel() {
        Park park = chooseInitialPark();
        List<Ride> rides = park == null ? null : ridesProvider.ridesForPark(park.getRcdbId());
        RidesData rdata = new RidesData(park, rides, loadCounts(rides));
        data.setValue(rdata);
    }

    /**
     * Informs that fields in the park or rides has changed
     */
    public void notifyFieldModification() {
        // Re-POST the existing value, presuming fields have changed
        RidesData existingValue = data.getValue();
        data.postValue(existingValue);
    }

    /**
     * Loads the counts for the givne rides from the Count Provider (usually the SQLite of the app).
     * All known counts will be returned.
     *
     * @param rides The rides
     * @return the Counts
     */
    private Map<PoiKey, Count> loadCounts(List<Ride> rides) {
        Map<PoiKey, Count> counts = new HashMap<>();
        for (Ride ride : rides) {
            counts.putAll(countProvider.getByPoiKey(Integer.toString(ride.rcdbId())));
        }
        return counts;
    }

    /**
     * Informs that fields in the park or rides has changed
     */
    public void setNewPark(Park parkNew, List<Ride> ridesNew, Map<PoiKey, Count> countsNew) {
        data.postValue(new RidesData(parkNew, ridesNew, countsNew));
    }


    public LiveData<RidesData> ridesData() {
        return data;
    }

    public void  notifyCountChange(Ride ride, Count count) {
        // Persist the count, writing it to DB
        countProvider.replaceCount(Integer.toString(ride.rcdbId()), count);
        RidesData existingValue = data.getValue();
        // Post the changed data, loading count back from DB. This will be a bit inefficient,
        // as counts for ALL rides of this Model are reloaded. Usually this is only a dozend or less,
        // but if the user would be able to show all rides (10000 in the whole world), this would be
        // not so efficient. For now we don't do this premature optimization.
        data.postValue(new RidesData(existingValue.park, existingValue.rides, loadCounts(existingValue.rides)));
    }


    private Park chooseInitialPark() {
        // We should pick the last park that the user had selected (taken from the DB).
        // Alternatively - for the first run - select the closest park or the biggest pork in the
        // country of the user.
        String cc2letter = Locale.getDefault().getCountry();
        Park park = chooseInitialPark(cc2letter);
        if (park == null) {
            // A user in a locale w/o parks will see something else. For now we pick "DE"
            park = chooseInitialPark("GB");
        }
        if (park == null) {
            // Still noting? Just pick the first park (if any)
            List<Park> allParks = parksProvider.all();
            park = allParks.isEmpty() ? null : allParks.get(0);
        }

        return park;
    }

    private Park chooseInitialPark(String cc2letter) {

        Park bestPark = null;
        int bestParkRideCount = 0;

        List<Park> allParks = parksProvider.all();
        if (allParks.isEmpty()) {
            return null;
        }

        for (Park park : allParks) {
            City city = cityProvider.byCityId(park.getCityId(), false);
            if (city != null && cc2letter.equals(city.getCountry2letter())) {
                List<Ride> rides = ridesProvider.ridesForPark(park.getRcdbId());
                int rideCount = rides.size();
                if (bestPark == null || rideCount > bestParkRideCount) {
                    bestPark = park;
                    bestParkRideCount = rideCount;
                }
            }
        }
        return bestPark;
    }
}