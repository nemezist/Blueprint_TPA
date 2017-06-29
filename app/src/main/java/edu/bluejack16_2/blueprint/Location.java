package edu.bluejack16_2.blueprint;

/**
 * Created by BAGAS on 29-Jun-17.
 */

public class Location {
    String placesId,placesName,placesType;

    public Location() {
    }

    public Location(String placesId, String placesName, String placesType) {
        this.placesId = placesId;
        this.placesName = placesName;
        this.placesType = placesType;
    }

    public String getPlacesId() {
        return placesId;
    }

    public void setPlacesId(String placesId) {
        this.placesId = placesId;
    }

    public String getPlacesName() {
        return placesName;
    }

    public void setPlacesName(String placesName) {
        this.placesName = placesName;
    }

    public String getPlacesType() {
        return placesType;
    }

    public void setPlacesType(String placesType) {
        this.placesType = placesType;
    }

}
