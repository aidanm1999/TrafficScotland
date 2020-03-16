package www.aidanm.trafficscotland.models.apimodels;

import www.aidanm.trafficscotland.models.enums.AsyncTaskCallUrlType;

public class AsyncTaskCallInput {
    private AsyncTaskCallUrlType type;
    private String url;

    // Overloaded Constructor
    public AsyncTaskCallInput(AsyncTaskCallUrlType Type, String Url){
        type = Type;
        url = Url;
    }

    public AsyncTaskCallUrlType getUrlType(){
        return type;
    }

    public String getUrl(){
        return url;
    }
}
