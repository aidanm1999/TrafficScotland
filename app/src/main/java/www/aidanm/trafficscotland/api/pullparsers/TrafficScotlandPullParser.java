package www.aidanm.trafficscotland.api.pullparsers;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import www.aidanm.trafficscotland.models.apimodels.TrafficScotlandChannel;
import www.aidanm.trafficscotland.models.apimodels.TrafficScotlandChannelItem;
import www.aidanm.trafficscotland.models.enums.TrafficScotlandPullParserScope;

public class TrafficScotlandPullParser {
    private TrafficScotlandChannel channel = new TrafficScotlandChannel();
    private TrafficScotlandChannelItem currentItem = new TrafficScotlandChannelItem();
    private TrafficScotlandPullParserScope scope = TrafficScotlandPullParserScope.Channel;
    private InputStream stream;

    public void setStream(InputStream input){
        stream = input;
    }

    public TrafficScotlandChannel execute(){

        sanitiseStringToParse();

        channel = new TrafficScotlandChannel();


        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            //xpp.setInput(new StringReader(stringToParse));
            xpp.setInput(stream, null);


            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {

                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if(xpp.getName() != null) {
                            Log.i("START_TAG", xpp.getName());
                        }
                        // This means a new object is being initialised
                        // First check what the object is and if it is CHANNEL, ITEM or COORDINATES
                        // If so, the scope must be changed to know what object is created
                        switch(xpp.getName().toLowerCase()){
                            case "channel":
                                scope = TrafficScotlandPullParserScope.Channel;
                                break;
                            case "item":
                                scope = TrafficScotlandPullParserScope.Item;
                                break;
                            case "georss:point":
                                scope = TrafficScotlandPullParserScope.Coordinates;
                                String latLogString = xpp.nextText();
                                if(!latLogString.isEmpty()){
                                    String[] latLongs = latLogString.split(" ");
                                    try {
                                        Double lat = Double.parseDouble(latLongs[0]);
                                        Double lon = Double.parseDouble(latLongs[1]);
                                        currentItem.setCoordinates(lat, lon);
                                    } catch (Exception e){}
                                }

                                scope = TrafficScotlandPullParserScope.Item;
                                break;

                            // This means we are not changing hierarchical state
                            // This will be an attribute of the current state's object
                            // Therefore we check what scope the attribute belongs to,
                            // Then the attribute is added to the object that the scope
                            // is set to.
                            case "title":
                                String title = xpp.nextText();
                                if(scope.equals(TrafficScotlandPullParserScope.Channel))
                                {
                                    channel.setTitle(title);
                                } else {
                                    currentItem.setTitle(title);
                                }
                                break;
                            case "description":
                                String description = xpp.nextText();
                                if(scope.equals(TrafficScotlandPullParserScope.Channel))
                                {
                                    channel.setDescription(description);
                                } else {
                                    currentItem.setDescription(description);
                                }
                                break;
                            case "link":
                                String link = xpp.nextText();
                                if(scope.equals(TrafficScotlandPullParserScope.Channel))
                                {
                                    channel.setLink(link);
                                } else {
                                    currentItem.setLink(link);
                                }
                                break;
                            case "ttl":
                                // Only works with Channel
                                String ttl = xpp.nextText();
                                channel.setTtl(Integer.parseInt(ttl));
                                break;
                            case "pubDate":
                                // Only works with ChannelItem
                                // Wed, 01 Jan 2020 00:00:00 GMT
                                // Tue, 02 Jan 2018 18:07:59 IST
                                String pubDate = xpp.nextText();
                                try{
                                    Date date = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z").parse(pubDate);
                                    currentItem.setDatePublished(date);
                                }catch (Exception e){

                                }

                                break;

                            default:
                                // Do nothing
                                // This label is not used
                                break;
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if(xpp.getName() != null) {
                            Log.i("END_TAG", xpp.getName());
                        }
                        if(xpp.getName().toLowerCase().equalsIgnoreCase("item") && scope == TrafficScotlandPullParserScope.Item){
                            // The item is over, add item to list, create new empty item, set new scope back to the channel
                            Log.i("Added", "Channel Item");
                            channel.addItem(currentItem);
                            currentItem = new TrafficScotlandChannelItem();
                            scope = TrafficScotlandPullParserScope.Channel;
                        }
                        break;
                    default:
                        if(xpp.getName() != null) {
                            Log.i("OTHER_TAG", xpp.getName());
                        }
                        break;
                }

                eventType = xpp.next();
            }
        } catch (XmlPullParserException ae1)
        {
            Log.e("MyTag","Parsing error" + ae1.toString());

        } catch (IOException ae1)
        {
            Log.e("MyTag","IO error during parsing");

        } catch(Exception e){
            Log.e("MyTag","Other error");
        }




        return channel;
    }


    public void sanitiseStringToParse(){
        // TODO - All that is being done is the removal of any 'georss:' in a georss:point tag

    }
}
