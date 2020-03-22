package www.aidanm.trafficscotland.models.apimodels;

public class TrafficScotlandAPIModel {
    private TrafficScotlandChannel channel;
    private AsyncTaskCallInput input;


    public TrafficScotlandAPIModel(TrafficScotlandChannel channel, AsyncTaskCallInput input) {
        this.channel = channel;
        this.input = input;
    }


    public AsyncTaskCallInput getInput() {
        return input;
    }

    public void setInput(AsyncTaskCallInput input) {
        this.input = input;
    }

    public TrafficScotlandChannel getChannel() {
        return channel;
    }

    public void setChannel(TrafficScotlandChannel channel) {
        this.channel = channel;
    }
}
