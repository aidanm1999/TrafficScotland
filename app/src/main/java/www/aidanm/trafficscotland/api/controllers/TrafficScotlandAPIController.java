package www.aidanm.trafficscotland.api.controllers;

import www.aidanm.trafficscotland.api.tasks.HttpGetTrafficScotlandRequest;
import www.aidanm.trafficscotland.controllers.LookController;
import www.aidanm.trafficscotland.controllers.PlannerController;
import www.aidanm.trafficscotland.controllers.TodayController;
import www.aidanm.trafficscotland.models.apimodels.AsyncTaskCallInput;
import www.aidanm.trafficscotland.models.apimodels.TrafficScotlandAPIModel;
import www.aidanm.trafficscotland.models.apimodels.TrafficScotlandChannel;
import www.aidanm.trafficscotland.models.enums.AsyncTaskCallUrlType;
import www.aidanm.trafficscotland.models.enums.TrafficScotlandSourceViewRequest;

public class TrafficScotlandAPIController {
    final private String currentIncidentsUrl = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    final private String roadWorksUrl = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    final private String plannedRoadWorksUrl = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";



    String result;


    public void getCurrentIncidents(TrafficScotlandSourceViewRequest viewRequest, TodayController controller){
        HttpGetTrafficScotlandRequest getRequest = new HttpGetTrafficScotlandRequest();
        AsyncTaskCallInput input = new AsyncTaskCallInput(
                AsyncTaskCallUrlType.TrafficScotland_CurrentIncidents,
                currentIncidentsUrl,
                viewRequest
        );


        TrafficScotlandAPIModel model = new TrafficScotlandAPIModel( new TrafficScotlandChannel(), input );
        getRequest.delegate = controller;
        getRequest.execute(model);
    }

    public void getCurrentIncidents(TrafficScotlandSourceViewRequest viewRequest, LookController controller){
        HttpGetTrafficScotlandRequest getRequest = new HttpGetTrafficScotlandRequest();
        AsyncTaskCallInput input = new AsyncTaskCallInput(
                AsyncTaskCallUrlType.TrafficScotland_CurrentIncidents,
                currentIncidentsUrl,
                viewRequest
        );


        TrafficScotlandAPIModel model = new TrafficScotlandAPIModel( new TrafficScotlandChannel(), input );
        getRequest.delegate = controller;
        getRequest.execute(model);
    }

    public void getCurrentIncidents(TrafficScotlandSourceViewRequest viewRequest, PlannerController controller){
        HttpGetTrafficScotlandRequest getRequest = new HttpGetTrafficScotlandRequest();
        AsyncTaskCallInput input = new AsyncTaskCallInput(
                AsyncTaskCallUrlType.TrafficScotland_CurrentIncidents,
                currentIncidentsUrl,
                viewRequest
        );


        TrafficScotlandAPIModel model = new TrafficScotlandAPIModel( new TrafficScotlandChannel(), input );
        getRequest.delegate = controller;
        getRequest.execute(model);
    }





    public void getRoadWorks(TrafficScotlandSourceViewRequest viewRequest, TodayController controller){
        HttpGetTrafficScotlandRequest getRequest = new HttpGetTrafficScotlandRequest();
        AsyncTaskCallInput input = new AsyncTaskCallInput(
                AsyncTaskCallUrlType.TrafficScotland_Roadworks,
                roadWorksUrl,
                viewRequest
        );

        TrafficScotlandAPIModel model = new TrafficScotlandAPIModel( new TrafficScotlandChannel(), input );
        getRequest.delegate = controller;
        getRequest.execute(model);
    }

    public void getRoadWorks(TrafficScotlandSourceViewRequest viewRequest, LookController controller){
        HttpGetTrafficScotlandRequest getRequest = new HttpGetTrafficScotlandRequest();
        AsyncTaskCallInput input = new AsyncTaskCallInput(
                AsyncTaskCallUrlType.TrafficScotland_Roadworks,
                roadWorksUrl,
                viewRequest
        );

        TrafficScotlandAPIModel model = new TrafficScotlandAPIModel( new TrafficScotlandChannel(), input );
        getRequest.delegate = controller;
        getRequest.execute(model);
    }

    public void getRoadWorks(TrafficScotlandSourceViewRequest viewRequest, PlannerController controller){
        HttpGetTrafficScotlandRequest getRequest = new HttpGetTrafficScotlandRequest();
        AsyncTaskCallInput input = new AsyncTaskCallInput(
                AsyncTaskCallUrlType.TrafficScotland_Roadworks,
                roadWorksUrl,
                viewRequest
        );

        TrafficScotlandAPIModel model = new TrafficScotlandAPIModel( new TrafficScotlandChannel(), input );
        getRequest.delegate = controller;
        getRequest.execute(model);
    }





    public void getPlannedRoadWorks(TrafficScotlandSourceViewRequest viewRequest){
        HttpGetTrafficScotlandRequest getRequest = new HttpGetTrafficScotlandRequest();
        AsyncTaskCallInput input = new AsyncTaskCallInput(
                AsyncTaskCallUrlType.TrafficScotland_PlannedRoadworks,
                plannedRoadWorksUrl,
                viewRequest
        );

        TrafficScotlandAPIModel model = new TrafficScotlandAPIModel( new TrafficScotlandChannel(), input );
        getRequest.execute(model);
    }


}
