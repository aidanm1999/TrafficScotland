package www.aidanm.trafficscotland.models.interfaces;

// Developer: Aidan Marshall
// Student ID: S1828601

import www.aidanm.trafficscotland.models.apimodels.TrafficScotlandAPIModel;

public interface AsyncResponse {
    void processFinish(TrafficScotlandAPIModel output);
}