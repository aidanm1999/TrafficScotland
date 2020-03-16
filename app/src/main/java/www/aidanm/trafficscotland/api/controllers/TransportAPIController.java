package www.aidanm.trafficscotland.api.controllers;

import java.util.ArrayList;
import java.util.List;

import www.aidanm.trafficscotland.models.apimodels.TransportAPIBusTimes;
import www.aidanm.trafficscotland.models.apimodels.TransportAPITrainTimes;

public class TransportAPIController {
    private String apiKey = "2ff5ad32ecc52ad64906535693ab07c1";
    private String appId = "1525a1f0";

    private String startPoint;
    private String destination;
    private ArrayList<TransportAPIBusTimes> busTimes;
    private ArrayList<TransportAPITrainTimes> trainTimes;

    // This API Controller is responsible for delivering requests from the transportapi.com website
    // The API will call the Traffic Scotland PullParser which fetches and delivers data to this
    // method, then it gets pushed to the controller so it can be displayed on screen


    public List<TransportAPITrainTimes> getTrainTimes(){

        return trainTimes;
    }
    public List<TransportAPIBusTimes> getBusTimes(){

        return busTimes;
    }
}
