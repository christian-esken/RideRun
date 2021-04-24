package org.riderun.app.ui.parks;

import org.riderun.app.model.GeoCoordinate;
import org.riderun.app.model.Park;
import org.riderun.app.storage.mock.ParkMock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Business logic for the ParksFragment. Any modification of ParksData must go through this class
 */
public class ParksViewModel extends ViewModel {
    private final static int LIMIT = 50;
    private MutableLiveData<ParksData> parksData = new MutableLiveData<>();
    private ParkMock parkprovider;

    public ParksViewModel() {
        parkprovider = ParkMock.instance();
        ParksData rdata = new ParksData("", GeoCoordinate.empty(), LIMIT, Collections.emptyList());
        ParksData appliedFilter = applyFilter(rdata, parkprovider);
        parksData.setValue(appliedFilter);
    }

    /**
     * Returns Parks from the parkprovider that match the filter criteria. The returned list is
     * sorted by the sort criteria (e.g. distance) and limited with the limit criteria.
     *
     * Filters the Parks from the parkprovider
     * @param criteria Filter, Sort and Limit criteria
     * @param parkprovider The list of parks to take into account. It can be all parks, or a
     *                     limited list, e.g. the parks from aq given Country of Tour.
     * @return The matching, filtered and sorted Parks
     */
    private ParksData applyFilter(ParksData criteria, ParkMock parkprovider) {
        String nameFilter = criteria.nameFilter.toLowerCase();
        boolean hasNameFilter = !nameFilter.trim().isEmpty();
        boolean hasFilter = hasNameFilter; // currently there is only one filter
        GeoCoordinate geo = criteria.geoCoordinate;
        boolean hasGeosorting = !geo.isEmpty();
        boolean hasSorting = hasGeosorting;

        int limit = criteria.limit;
        List<Park> parksProviderParks = parkprovider.parks();
        List<Park> parksMatching = new ArrayList<>(parksProviderParks.size());

        if (hasFilter) {
            for (Park park : parksProviderParks) {
                if (hasNameFilter && park.getName().toLowerCase().contains(nameFilter)) {
                    parksMatching.add(park);
                }
            }
        } else {
            parksMatching.addAll(parksProviderParks); // not filtered => all parks
        }

        if (hasSorting) {
            if (hasGeosorting) {
                parksMatching.sort( (a,b) -> {
                    double distanceA = a.getGeoCoordinate().sortingDistance(geo);
                    double distanceB = b.getGeoCoordinate().sortingDistance(geo);
                    return distanceA < distanceB ? -1 : (distanceA == distanceB ? 0 : 1);
                });
            }
        }

        List<Park> parksLimited = parksMatching.subList(0,limit);

        ParksData finalPD = new ParksData(nameFilter,geo,limit,parksLimited);
        return finalPD;
    }

    public LiveData<ParksData> getParksData() {
        return parksData;
    }
}