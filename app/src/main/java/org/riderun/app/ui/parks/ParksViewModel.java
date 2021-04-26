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
    private MutableLiveData<ParksData> liveParksData = new MutableLiveData<>();
    private ParkMockReader parkProvider;

    public ParksViewModel() {
        // Set providers
        parkProvider = ParkMockReader.instance();

        // Note: For now use the ConfigDefaultsProvider. Later we should pick it from the user config.
        ConfigProvider config = new ConfigDefaultsProvider();
        ParksFilterCriteria filterCriteria = new ParksFilterCriteria.Builder()
                .preselection(config.parkPreselection())
                .geoCoordinate(config.geoCoordinate())
                .limit(config.parkLimit()).build();
        ParksData appliedFilter = applyFilter(filterCriteria, parkProvider);
        liveParksData.setValue(appliedFilter);
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
        // PRESELECTION : TODO Implement the other preselection methods (currently always: ALL)
        final List<Park> parkList;
        switch (criteria.preselection) {
            case All:
                parkList = parkprovider.parks();
                break;
            case Location:
                parkList = new ArrayList<>();
                String cc = criteria.locationCountryCode2letter;
                if (cc != null) {
                    for (Park park : parkprovider.parks()) {
                        if (cc.equals(park.getCity().getCountry2letter())) {
                            parkList.add(park);
                        }
                    }
                }
                // TODO Also implement city and continent preselection
                break;
            case Tour:
                throw new UnsupportedOperationException("Tour not yet implemented");
            case Nearby:
                throw new UnsupportedOperationException("Nearby not yet implemented");
            default:
                // TODO Log warning
                parkList = parkprovider.parks();
                break;
        }
        List<Park> parksMatching = new ArrayList<>(parkList.size());

        // WHERE
        if (hasFilter) {
            for (Park park : parkList) {
                if (hasNameFilter && park.getName().toLowerCase().contains(nameFilter)) {
                    parksMatching.add(park);
                }
            }
        } else {
            parksMatching.addAll(parkList); // not filtered => all parks
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
        List<Park> parksLimited = parksMatching.subList(0, Math.min(limit, parksMatching.size()));

        return new ParksData(criteria, parksLimited, parksMatching);
    }

    public void setParkNameFilter(String parkName) {
        postModifiedFilter(fc().nameFilter(parkName));
    }

    public void setLocationContinent(String continent) {
        postModifiedFilter(fc().locationContinent(continent));
    }

    public void setLocationCountry(String cc2letter) {
        postModifiedFilter(fc().locationCountryCode2letter(cc2letter));
    }

    public void setLocationCityId(Integer cityId) {
        postModifiedFilter(fc().locationCityId(cityId));
    }

    /**
     * Returns a new FilterCriteria.Builder from the current ParksFilterCriteria.
     * @return the builder
     */
    private ParksFilterCriteria.Builder fc() {
        ParksFilterCriteria fc = liveParksData.getValue().filterCriteria;
        return ParksFilterCriteria.Builder.builder(fc);
    }

    private void postModifiedFilter(ParksFilterCriteria.Builder fcBuilder) {
        ParksFilterCriteria fcNew = fcBuilder.build();
        ParksFilterCriteria fcOld = liveParksData.getValue().filterCriteria;
        if (!fcNew.equals(fcOld)) {
            // Filter criteria has been modified => notify the LiveData
            // Note: This has two goals: Avoid unnecessary GUI rebuilds, and avoid infinite "recursion".
            //  Infinite recursions could happen like this: User changes "park name"
            //  -> ParksFragment (see TextWatcher).
            //  -> TextWatcher.afterTextChanged()
            //  -> ParksViewModel.setParkNameFilter()
            //  -> ParksViewModel.postModifiedFilter()
            //  -> liveParksData.postValue(parksData)
            //  -> ParksFragment (see TextWatcher)
            //  ...
            ParksData parksData = applyFilter(fcNew, parkProvider);
            liveParksData.postValue(parksData);
        }
    }



    public LiveData<ParksData> getLiveParksData() {
        return liveParksData;
    }
}