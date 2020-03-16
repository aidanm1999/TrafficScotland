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
    private String stringToParse = "";
    private TrafficScotlandChannel channel = new TrafficScotlandChannel();
    private TrafficScotlandChannelItem currentItem = new TrafficScotlandChannelItem();
    private TrafficScotlandPullParserScope scope = TrafficScotlandPullParserScope.Channel;
    private InputStream stream;

    public void setStringToParse(String input){
        stringToParse = input;
    }

    public void setStream(InputStream input){
        stream = input;
    }

    public TrafficScotlandChannel execute(){

        sanitiseStringToParse();

        stringToParse =   "<channel>"+
        "<title>Traffic Scotland - Roadworks</title>"+
        "<description>Roadworks currently being undertaken on the road network.</description>"+
        "<link>https://trafficscotland.org/roadworks/</link>"+
        "<language />"+
        "<copyright />"+
        "<managingEditor />"+
        "<webMaster />"+
        "<lastBuildDate>Mon, 16 Mar 2020 00:00:00 GMT</lastBuildDate>"+
        "<docs>https://trafficscotland.org/rss/</docs>"+
        "<rating />"+
        "<generator>Traffic Scotland | www.trafficscotland.org</generator>"+
        "<ttl>5</ttl>"+
        "<item>"+
            "<title>M90 J1 to J3</title>"+
            "<description>Start Date: Wednesday, 01 January 2020 - 00:00&lt;br /&gt;End Date: Tuesday, 31 March 2020 - 00:00&lt;br /&gt;Delay Information: No reported delay.</description>"+
            "<link>http://tscot.org/03cFB2019700</link>"+
            "<point>56.0367767647623 -3.40822032632123</point>"+
            "<author />"+
            "<comments />"+
            "<pubDate>Wed, 01 Jan 2020 00:00:00 GMT</pubDate>"+
        "</item>"+
        "<item>"+
            "<title>M90 J1 to J3</title>"+
            "<description>Start Date: Wednesday, 01 January 2020 - 00:00&lt;br /&gt;End Date: Tuesday, 31 March 2020 - 00:00&lt;br /&gt;Delay Information: No reported delay.</description>"+
            "<link>http://tscot.org/03cFB2019701</link>"+
            "<point>56.0732980724831 -3.38885965373524</point>"+
            "<author />"+
            "<comments />"+
            "<pubDate>Wed, 01 Jan 2020 00:00:00 GMT</pubDate>"+
        "</item>"+
    "</channel>";

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
                if(xpp.getName() != null) {
                    Log.i("START_TAG", xpp.getName());
                }

                switch (eventType){
                    case XmlPullParser.START_TAG:
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
                            case "point":
                                scope = TrafficScotlandPullParserScope.Coordinates;
                                // TODO - Add
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
                        Log.i("END_TAG", xpp.getName()); //equalsIgnoreCase(String anotherString)
                        if(xpp.getName().toLowerCase().equalsIgnoreCase("item") && scope == TrafficScotlandPullParserScope.Item){
                            // The item is over, add item to list, create new empty item, set new scope back to the channel
                            Log.i("Added", "Channel Item");
                            channel.addItem(currentItem);
                            currentItem = new TrafficScotlandChannelItem();
                            scope = TrafficScotlandPullParserScope.Channel;
                        }
                        break;
                    default:
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

    }
}
