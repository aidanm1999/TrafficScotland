package www.aidanm.trafficscotland.models.apimodels;

import com.google.android.gms.maps.model.LatLng;

public class TrafficScotlandCoordinates {
    private double Latitude;
    private double Longitude;

    // Empty Constructor
    public TrafficScotlandCoordinates() {
        Latitude = 0;
        Longitude = 0;
    }

    // Overloaded Constructor
    public TrafficScotlandCoordinates(double latitude, double longitude) {
        Latitude = latitude;
        Longitude = longitude;
    }

    // No getters and setters

    // Methods
    public LatLng getCoordinates(){
        return new LatLng(Latitude, Longitude);
    }
}
