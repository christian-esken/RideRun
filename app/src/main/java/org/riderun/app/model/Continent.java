package org.riderun.app.model;

public enum Continent {
    Africa(1), America(2), Asia(3), Oceania(4), Europe(5),
    NorthAmerica(6,2), SouthAmerica(7,2);

    private final int id;
    private final int parent;

    Continent(int id) {
        this(id, 0);
    }

    Continent(int id, int parent) {
        this.id = id;
        this.parent = parent;
    }


}
