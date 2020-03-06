package www.aidanm.trafficscotland.models.apimodels;

import java.util.ArrayList;
import java.util.List;

public class TrafficScotlandChannel {
    private String Title;
    private String Description;
    private String Link;
    private int Ttl;
    private ArrayList<TrafficScotlandChannelItem> ChannelItems;



    // Empty Constructor
    public TrafficScotlandChannel() {
        Title = "";
        Description = "";
        Link = "";
        Ttl = 0;
        ChannelItems = new ArrayList<TrafficScotlandChannelItem>();
    }

    // Overloaded Constructor
    public TrafficScotlandChannel(String title, String description, String link, int ttl, ArrayList<TrafficScotlandChannelItem> channelItems){
        Title = title;
        Description = description;
        Link = link;
        Ttl = ttl;
        ChannelItems = channelItems;
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

    public int getTtl() {
        return Ttl;
    }

    public void setTtl(int ttl) {
        Ttl = ttl;
    }

    public List<TrafficScotlandChannelItem> getChannelItems() {
        return ChannelItems;
    }

    public void setChannelItems(ArrayList<TrafficScotlandChannelItem> channelItems) {
        ChannelItems = channelItems;
    }


    // Methods
    public void addItem(TrafficScotlandChannelItem item){
        ChannelItems.add(item);
    }
}
