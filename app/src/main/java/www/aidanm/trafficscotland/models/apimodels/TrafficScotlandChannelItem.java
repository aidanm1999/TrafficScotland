package www.aidanm.trafficscotland.models.apimodels;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Date;

public class TrafficScotlandChannelItem {

    private String Title;
    private String Description;
    private String Link;
    private TrafficScotlandCoordinates Coordinates;
    private Date DatePublished;


    // Empty Constructor
    public TrafficScotlandChannelItem() {
        Title = "";
        Description = "";
        Link = "";
        Coordinates = new TrafficScotlandCoordinates();

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        DatePublished = today.getTime();
    }


    // Overloaded Constructor
    public TrafficScotlandChannelItem(String title, String description, String link, TrafficScotlandCoordinates coordinates){
        Title = title;
        Description = description;
        Link = link;
        Coordinates = coordinates;
    }




    // Getters and Setters
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }


    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public LatLng getCoordinates() {
        return Coordinates.getCoordinates();
    }

    public void setCoordinates(double latitude, double longitude) {
        Coordinates = new TrafficScotlandCoordinates(latitude, longitude);
    }
}
