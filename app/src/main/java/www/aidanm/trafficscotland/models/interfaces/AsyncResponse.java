package www.aidanm.trafficscotland.models.interfaces;

import www.aidanm.trafficscotland.models.apimodels.TrafficScotlandAPIModel;

public interface AsyncResponse {
    void processFinish(TrafficScotlandAPIModel output);
}