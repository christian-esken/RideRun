package org.riderun.app.ui.parks;

import org.riderun.app.model.GeoCoordinate;
import org.riderun.app.model.Park;
import org.riderun.app.provider.config.ConfigDefaultsProvider;
import org.riderun.app.provider.config.ConfigProvider;
import org.riderun.app.provider.park.mock.ParkMockReader;

import java.util.ArrayList;
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
    private ParkMockReader parkProvider;

    public ParksViewModel() {
        // Set providers
        parkProvider = ParkMockReader.instance();

        // Note: For now use the ConfigDefaultsProvider. Later we should pick it from the user config.
        ConfigProvider config = new ConfigDefaultsProvider();
        ParksFilterCriteria filterCriteria = new ParksFilterCriteria(config.parkPreselection(), "", config.geoCoordinate(), config.parkLimit());
        // Hint: The filter will select
        ParksData appliedFilter = applyFilter(filterCriteria, parkProvider);
        parksData.setValue(appliedFilter);
    }

    /**
     * Returns Parks from the parkprovider that match the filter criteria. The returned list is
     * sorted by the sort criteria (e.g. distance) and limited with the limit criteria.
     *
     * Filters the Parks from the parkprovider
     * @param criteria Filter, Sort and Limit criteria, and the park list
     * @param parkprovider The list of parks to take into account. It can be all parks, or a
     *                     limited list, e.g. the parks from aq given Country of Tour.
     * @return The matching, filtered and sorted Parks
     */
    private ParksData applyFilter(ParksFilterCriteria criteria, ParkMockReader parkprovider) {
        String nameFilter = criteria.nameFilter.toLowerCase();
        boolean hasNameFilter = !nameFilter.trim().isEmpty();
        boolean hasFilter = hasNameFilter; // currently there is only one filter
        GeoCoordinate geo = criteria.geoCoordinate;
        boolean hasGeosorting = !geo.isEmpty();
        boolean hasSorting = hasGeosorting;

        int limit = criteria.limit;
        List<Park> parksProviderParks = parkprovider.parks();
        List<Park> parksMatching = new ArrayList<>(parksProviderParks.size());

        // WHERE
        if (hasFilter) {
            for (Park park : parksProviderParks) {
                if (hasNameFilter && park.getName().toLowerCase().contains(nameFilter)) {
                    parksMatching.add(park);
                }
            }
        } else {
            parksMatching.addAll(parksProviderParks); // not filtered => all parks
        }

        // ORDER BY
        if (hasSorting) {
            if (hasGeosorting) {
                parksMatching.sort( (a,b) -> {
                    double distanceA = a.getGeoCoordinate().sortingDistance(geo);
                    double distanceB = b.getGeoCoordinate().sortingDistance(geo);
                    return distanceA < distanceB ? -1 : (distanceA == distanceB ? 0 : 1);
                });
            }
        }

        // LIMIT
        List<Park> parksLimited = parksMatching.subList(0,limit);

        return new ParksData(criteria, parksLimited);
    }

    public LiveData<ParksData> getParksData() {
        return parksData;
    }
}