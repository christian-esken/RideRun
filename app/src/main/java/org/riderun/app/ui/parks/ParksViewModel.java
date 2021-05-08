package org.riderun.app.ui.parks;

import org.riderun.app.model.City;
import org.riderun.app.model.GeoCoordinate;
import org.riderun.app.model.Park;
import org.riderun.app.model.ParkUserData;
import org.riderun.app.provider.ProviderFactory;
import org.riderun.app.provider.city.CityProvider;
import org.riderun.app.provider.config.ConfigDefaultsProvider;
import org.riderun.app.provider.config.ConfigProvider;
import org.riderun.app.provider.park.ParksProvider;
import org.riderun.app.provider.parkuserdata.ParksUserDataProvider;
import org.riderun.app.provider.ride.RidesProvider;

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
    private final MutableLiveData<ParksData> liveParksData = new MutableLiveData<>();
    private final ParksProvider parksProvider;
    private final ParksUserDataProvider parksUserDataProvider = ProviderFactory.parksUserDataProvider();
    private final CityProvider cityProvider;
    private final RidesProvider ridesProvider = ProviderFactory.ridesProvider();

    public ParksViewModel() {
        // Set providers
        parksProvider = ProviderFactory.parksProvider();
        cityProvider = ProviderFactory.cityProvider();

        // Note: For now use the ConfigDefaultsProvider. Later we should pick it from the user config.
        ConfigProvider config = new ConfigDefaultsProvider();
        ParksFilterCriteria filterCriteria = ParksFilterCriteria.Builder.builder()
                .preselection(config.parkPreselection())
                .geoCoordinate(config.geoCoordinate())
                .orderBy(config.orderBy())
                .orderDirection(config.orderDirection())
                .limit(config.parkLimit())
                .modificationHint(ParksFilterCriteria.ModificationHint.All)
                .build();
        ParksData appliedFilter = applyFilter(filterCriteria, parksProvider);
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
    private ParksData applyFilter(ParksFilterCriteria criteria, ParksProvider parkprovider) {
        String nameFilter = criteria.parkNameFilter.toLowerCase();
        boolean hasNameFilter = !nameFilter.trim().isEmpty();
        boolean hasFilter = hasNameFilter; // currently there is only one filter
        GeoCoordinate geo = criteria.geoCoordinate;

        int limit = criteria.limit;
        // PRESELECTION : TODO Implement the other preselection methods (currently always: ALL)
        final List<Park> parkList;
        switch (criteria.preselection) {
            case Likes:
                parkList = new ArrayList<>();
                for (Park park : parkprovider.all()) {
                    ParkUserData pud = parksUserDataProvider.byRcdbId(park.getRcdbId());
                    if(pud != null && pud.getLiked()) {
                        parkList.add(park);
                    }
                }

                break;
            case Location:
                parkList = new ArrayList<>();
                String cc = criteria.locationCountryCode2letter;
                Integer cityId = criteria.locationCityId;
                List<Park> parks = parkprovider.all();
                if (cc != null || cityId != null) {
                    for (Park park : parks) {
                        // TODO Check if assignUnknownCityIfNotFound should be true or false here
                        City city = cityProvider.byCityId(park.getCityId(), true);
                        if (cc == null || cc.equals(city.getCountry2letter())) {
                            // country match
                            if (cityId == null || cityId == city.getCityId()) {
                                // city match
                                parkList.add(park);
                            }
                        }
                    }
                } else {
                    parkList.addAll(parks);
                }
                // TODO Also implement city and continent preselection
                break;
            case Tour:
                throw new UnsupportedOperationException("Tour not yet implemented");
            case Nearby:
                throw new UnsupportedOperationException("Nearby not yet implemented");
            default:
                // TODO Log warning
                parkList = parkprovider.all();
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
        OrderBy orderBy = criteria.orderBy;
        int orderMultiplier = criteria.orderDirection.multiplier();
        switch (orderBy) {
            case Distance:
                parksMatching.sort( (a,b) -> {
                    double distanceA = a.getGeoCoordinate().sortingDistance(geo);
                    double distanceB = b.getGeoCoordinate().sortingDistance(geo);
                    int order = distanceA < distanceB ? -1 : (distanceA == distanceB ? 0 : 1);
                    return orderMultiplier * order;
                });
                break;
            case Name:
                parksMatching.sort( (a,b) -> orderMultiplier * a.getName().compareTo(b.getName()));
                break;
            case AttractionCount:
                parksMatching.sort( (a,b) -> {
                    int countA = ridesProvider.ridesForPark(a.getRcdbId()).size();
                    int countB = ridesProvider.ridesForPark(b.getRcdbId()).size();
                    return orderMultiplier * (countB - countA);
                });
                break;
            default:
                // Do not sort. Keep parksMatching in its order.
                break;
        }

        // LIMIT
        List<Park> parksLimited = parksMatching.subList(0, Math.min(limit, parksMatching.size()));

        return new ParksData(criteria, parksLimited, parksMatching);
    }


    public void setPreselection(ParksPreselection preselection) {
        postModifiedFilter(fc().preselection(preselection));
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

    public void setNewOrder(OrderBy orderBy) {
        ParksFilterCriteria.Builder fc = fc();
        if (fc.orderBy == orderBy) {
            // If the order by criteria is the same as before, we toggle the order direction.
            postModifiedFilter(fc.orderDirection(fc.orderDirection.reverse()));
        } else {
            // user wants a new order.select the new order with its default order direction
            postModifiedFilter(fc().orderBy(orderBy).orderDirection(orderBy.orderDirection()));
        }
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
            ParksData parksData = applyFilter(fcNew, parksProvider);
            liveParksData.postValue(parksData);
        }
    }



    public LiveData<ParksData> getLiveParksData() {
        return liveParksData;
    }

}