package www.aidanm.trafficscotland.models.apimodels;

// Developer: Aidan Marshall
// Student ID: S1828601

import www.aidanm.trafficscotland.models.enums.AsyncTaskCallUrlType;
import www.aidanm.trafficscotland.models.enums.TrafficScotlandSourceViewRequest;

public class AsyncTaskCallInput {
    private AsyncTaskCallUrlType type;
    private String url;
    private TrafficScotlandSourceViewRequest viewRequest;

    // Overloaded Constructor
    public AsyncTaskCallInput(AsyncTaskCallUrlType Type, String Url, TrafficScotlandSourceViewRequest ViewRequest){
        type = Type;
        url = Url;
        viewRequest = ViewRequest;
    }

    public AsyncTaskCallUrlType getUrlType(){ return type; }

    public String getUrl(){
        return url;
    }
}
