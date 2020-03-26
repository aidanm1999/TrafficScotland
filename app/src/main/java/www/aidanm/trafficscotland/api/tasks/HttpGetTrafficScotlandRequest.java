package www.aidanm.trafficscotland.api.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import www.aidanm.trafficscotland.R;
import www.aidanm.trafficscotland.api.pullparsers.TrafficScotlandPullParser;
import www.aidanm.trafficscotland.models.apimodels.AsyncTaskCallInput;
import www.aidanm.trafficscotland.models.apimodels.TrafficScotlandAPIModel;
import www.aidanm.trafficscotland.models.apimodels.TrafficScotlandChannel;
import www.aidanm.trafficscotland.models.interfaces.AsyncResponse;

public class HttpGetTrafficScotlandRequest extends AsyncTask<TrafficScotlandAPIModel, Void, TrafficScotlandAPIModel> {

    public AsyncResponse delegate = null;

    ProgressDialog progressDialog;
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    private XmlPullParserFactory xmlFactoryObject;
    private TrafficScotlandChannel channel = new TrafficScotlandChannel();
    private TextInputEditText dateInput;
    private EditText editTxt;
    private Button btn;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;


    @Override
    protected TrafficScotlandAPIModel doInBackground(TrafficScotlandAPIModel... params) {
        String urlString = params[0].getInput().getUrl().toString();
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

        TrafficScotlandAPIModel model = new TrafficScotlandAPIModel(
                channel,
                params[0].getInput()
        );

        return model;
    }

    @Override
    protected void onPostExecute(TrafficScotlandAPIModel result) {
        delegate.processFinish(result);
    }
}
