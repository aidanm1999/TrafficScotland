package www.aidanm.trafficscotland.api.controllers;

import www.aidanm.trafficscotland.api.tasks.HttpGetRequest;
import www.aidanm.trafficscotland.models.apimodels.AsyncTaskCallInput;
import www.aidanm.trafficscotland.models.enums.AsyncTaskCallUrlType;

public class TrafficScotlandAPIController {
    final private String currentIncidentsUrl = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    final private String roadWorksUrl = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    final private String plannedRoadWorksUrl = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";



    String result;


    public void getCurrentIncidents(){
        HttpGetRequest getRequest = new HttpGetRequest();
        AsyncTaskCallInput input = new AsyncTaskCallInput(
                AsyncTaskCallUrlType.TrafficScotland_CurrentIncidents,
                currentIncidentsUrl
        );
        //result = getRequest.execute(input);
        getRequest.execute(input);
    }

    public void getRoadWorks(){
        HttpGetRequest getRequest = new HttpGetRequest();
        AsyncTaskCallInput input = new AsyncTaskCallInput(
                AsyncTaskCallUrlType.TrafficScotland_Roadworks,
                roadWorksUrl
        );
        //result = getRequest.execute(input);
        getRequest.execute(input);
    }

    public void getPlannedRoadWorks(){
        HttpGetRequest getRequest = new HttpGetRequest();
        AsyncTaskCallInput input = new AsyncTaskCallInput(
                AsyncTaskCallUrlType.TrafficScotland_PlannedRoadworks,
                plannedRoadWorksUrl
        );
        //result = getRequest.execute(input);
        getRequest.execute(input);
    }


}
