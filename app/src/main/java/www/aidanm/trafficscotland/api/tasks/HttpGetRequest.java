package www.aidanm.trafficscotland.api.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import www.aidanm.trafficscotland.api.pullparsers.TrafficScotlandPullParser;
import www.aidanm.trafficscotland.models.apimodels.AsyncTaskCallInput;
import www.aidanm.trafficscotland.models.apimodels.TrafficScotlandChannel;

public class HttpGetRequest extends AsyncTask<AsyncTaskCallInput, Void, TrafficScotlandChannel> {

    ProgressDialog progressDialog;
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    private XmlPullParserFactory xmlFactoryObject;
    private TrafficScotlandChannel channel = new TrafficScotlandChannel();


    @Override
    protected TrafficScotlandChannel doInBackground(AsyncTaskCallInput... params) {
        String urlString = params[0].getUrl().toString();
        String result;
        String inputLine;

        try {

            URL url = new URL(urlString.trim());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setDoInput(true);
            connection.connect();
            InputStream stream = connection.getInputStream();


            TrafficScotlandPullParser pullParser = new TrafficScotlandPullParser();
            pullParser.setStream(stream);
            channel = pullParser.execute();

            stream.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return channel;
    }

    @Override
    protected void onPostExecute(TrafficScotlandChannel result) {
        super.onPostExecute(result);
        Log.i("hy", "result");
    }
}
