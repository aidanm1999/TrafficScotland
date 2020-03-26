package www.aidanm.trafficscotland.models.apimodels;

// Developer: Aidan Marshall
// Student ID: S1828601

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Date;

import www.aidanm.trafficscotland.models.enums.AsyncTaskCallUrlType;

public class TrafficScotlandChannelItem {

    private String Title;
    private String Description;
    private String Link;
    private TrafficScotlandCoordinates Coordinates;
    private Date DatePublished;
    private Date StartDate;
    private Date EndDate;
    private AsyncTaskCallUrlType Type;


    // Empty Constructor
    public TrafficScotlandChannelItem() {
        Title = "";
        Description = "";
        Link = "";
        Coordinates = new TrafficScotlandCoordinates();

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        DatePublished = today.getTime();
        StartDate = today.getTime();
        EndDate = today.getTime();
    }


    // Overloaded Constructor
    public TrafficScotlandChannelItem(String title,
                                      String description,
                                      String link,
                                      TrafficScotlandCoordinates coordinates,
                                      Date datePublished,
                                      Date startDate,
                                      Date endDate,
                                      AsyncTaskCallUrlType type)
    {
        Title = title;
        Description = description;
        Link = link;
        Coordinates = coordinates;
        DatePublished = datePublished;
        StartDate = startDate;
        EndDate = endDate;
        Type = type;
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

    public Date getDatePublished() {
        return DatePublished;
    }

    public void setDatePublished(Date datePublished) {
        DatePublished = datePublished;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date endDate) {
        EndDate = endDate;
    }

    public AsyncTaskCallUrlType getType() {
        return Type;
    }

    public void setType(AsyncTaskCallUrlType type) {
        Type = type;
    }
}
