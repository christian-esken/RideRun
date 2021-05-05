package org.riderun.app.ui.parks;

import org.riderun.app.storage.Order;

public enum OrderBy {
    None, Name, AttractionCount, Distance;

    /**
     * Returns the default order direction for this OrderBy
     * @return the default order direction for this OrderBy
     */
    Order orderDirection() {
        switch (this) {
            case Name:
                return Order.ASC;
            case Distance:
            case AttractionCount:
                return Order.DESC;
            default:
                return Order.ASC;
        }
    }

}
