package www.aidanm.trafficscotland.controllers;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.maps.android.SphericalUtil;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

import www.aidanm.trafficscotland.MainActivity;
import www.aidanm.trafficscotland.R;
import www.aidanm.trafficscotland.api.controllers.TrafficScotlandAPIController;
import www.aidanm.trafficscotland.controllers.helpers.FetchURL;
import www.aidanm.trafficscotland.controllers.helpers.FormToastHelper;
import www.aidanm.trafficscotland.controllers.helpers.TaskLoadedCallback;
import www.aidanm.trafficscotland.models.apimodels.TrafficScotlandAPIModel;
import www.aidanm.trafficscotland.models.apimodels.TrafficScotlandChannelItem;
import www.aidanm.trafficscotland.models.enums.TrafficScotlandSourceViewRequest;
import www.aidanm.trafficscotland.models.interfaces.AsyncResponse;
import www.aidanm.trafficscotland.models.viewmodels.planner.PlannerViewModel;

public class PlannerController extends Fragment implements OnMapReadyCallback, AsyncResponse, TaskLoadedCallback {

    // region Fields
    private View root;
    private GoogleMap map;
    private MapView mapView;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Button submitButton;
    private TextInputEditText startText;
    private TextInputEditText endText;
    private TextInputLayout startLayout;
    private TextInputLayout endLayout;
    private FormToastHelper toastHelper;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;
    private LatLng startLatLong, endLatLong, medianLatLong;
    private MarkerOptions startPlace, endPlace;
    private Polyline currentPolyline;

    private int ZOOM_LEVEL = 12;
    // endregion

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // region Find all Views By Id
        root = inflater.inflate(R.layout.fragment_planner, container, false);
        startLayout = root.findViewById(R.id.planner_start_layout);
        endLayout = root.findViewById(R.id.planner_end_layout);
        startText = root.findViewById(R.id.planner_start_field);
        endText = root.findViewById(R.id.planner_end_field);
        submitButton = root.findViewById(R.id.planner_search_submit);
        // endregion

        // region Instantiate - Search Button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm(v);
            }
        });
        // endregion

        // region Instantiate - Places Client
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        Places.initialize(getContext(), "AIzaSyB-wEH8r3o-6Ajme-I33bBUGx-9wuxGgyI");
        placesClient = Places.createClient(getContext());
        // endregion

        // region Instantiate - Map
        MapsInitializer.initialize(this.getActivity());
        mapView = root.findViewById(R.id.planner_map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync((OnMapReadyCallback) this);
        // endregion

        // region Instantiate FormToastHelper
        toastHelper = new FormToastHelper(getContext());
        // endregion

        return root;
    }


    // region Map Recycle Methods
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng uk = new LatLng(54, -3);
        map.moveCamera(CameraUpdateFactory.newLatLng(uk));
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }
    // endregion


    // region Submit Form Methods
    private void submitForm(View root){
        boolean failedValidation = false;

        if(startText.getText().toString() == null || startText.getText().toString().equals("")){
            toastHelper.inputError("Filter");
            failedValidation = true;
        }

        if(endText.getText().toString() == null || endText.getText().toString().equals("")){
            toastHelper.inputError("Search");
            failedValidation = true;
        }

        if(!failedValidation){
            startText.onEditorAction(EditorInfo.IME_ACTION_DONE);
            endText.onEditorAction(EditorInfo.IME_ACTION_DONE);

            // region Find Start Place
            final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
            final FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest
                    .builder()
                    .setCountry("GB")
                    .setTypeFilter(TypeFilter.ADDRESS)
                    .setSessionToken(token)
                    .setQuery(startText.getText().toString() + " Scotland")
                    .build();



            placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
                    if(task.isSuccessful()){
                        FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
                        if(predictionsResponse != null) {
                            predictionList = predictionsResponse.getAutocompletePredictions();
                            if (predictionList.size() > 0) {
                                String placeId = predictionList.get(0).getPlaceId();
                                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);


                                final FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, placeFields);

                                placesClient.fetchPlace(placeRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                                    @Override
                                    public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                                        startLatLong = fetchPlaceResponse.getPlace().getLatLng();


                                        // region Find End Place
                                        final AutocompleteSessionToken token2 = AutocompleteSessionToken.newInstance();
                                        final FindAutocompletePredictionsRequest predictionsRequest2 = FindAutocompletePredictionsRequest
                                                .builder()
                                                .setCountry("GB")
                                                .setTypeFilter(TypeFilter.ADDRESS)
                                                .setSessionToken(token)
                                                .setQuery(endText.getText().toString() + " Scotland")
                                                .build();

                                        placesClient.findAutocompletePredictions(predictionsRequest2).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                                            @Override
                                            public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
                                                if(task.isSuccessful()){
                                                    FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
                                                    if(predictionsResponse != null) {
                                                        predictionList = predictionsResponse.getAutocompletePredictions();
                                                        if (predictionList.size() > 0) {
                                                            String placeId = predictionList.get(0).getPlaceId();
                                                            List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);


                                                            final FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, placeFields);

                                                            placesClient.fetchPlace(placeRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                                                                @Override
                                                                public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                                                                    endLatLong = fetchPlaceResponse.getPlace().getLatLng();

                                                                    calculateMedianLocation();
                                                                    updateMapCamera();


                                                                    // region Add Road Path
                                                                    startPlace = new MarkerOptions().position(startLatLong).title("Start");
                                                                    endPlace = new MarkerOptions().position(endLatLong).title("End");
                                                                    map.addMarker(startPlace);
                                                                    map.addMarker(endPlace);

                                                                    String url = getUrl(startPlace.getPosition(), endPlace.getPosition(),"driving");
                                                                    try {
                                                                        Context context = getContext();
                                                                        Context i = getActivity().getApplicationContext();
                                                                        Context c = getActivity().getFragmentManager().findFragmentById(R.id.navigation_journey_planner).getContext();


                                                                        new FetchURL(i).execute(url, "driving");
                                                                    }catch (Exception e){
                                                                        Log.i("", e.toString());
                                                                    }
                                                                    // endregion



                                                                    // region Add Incidents and Roadworks

                                                                    // endregion
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    toastHelper.custom("Location does not exist");
                                                                }
                                                            });
                                                        } else {
                                                            toastHelper.custom("No location found");
                                                        }
                                                    }
                                                } else {
                                                    Log.i("error", "prediction fetching task unsuccessful");
                                                }
                                            }
                                        });
                                        // endregion


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        toastHelper.custom("Location does not exist");
                                    }
                                });
                            } else {
                                toastHelper.custom("No location found");
                            }
                        }
                    } else {
                        Log.i("error", "prediction fetching task unsuccessful");
                    }
                }
            });
            // endregion
        }
    }


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyB-wEH8r3o-6Ajme-I33bBUGx-9wuxGgyI";// getString(R.string.google_maps_key);
        return url;
    }


    private void calculateMedianLocation(){
        Double lat = (startLatLong.latitude + endLatLong.latitude)/2;
        Double lon = (startLatLong.longitude + endLatLong.longitude)/2;
        medianLatLong = new LatLng(lat,lon);
    }



    private void updateMapCamera(){
        map.clear();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(medianLatLong, ZOOM_LEVEL));
    }

    private void callTrafficScotlandApi(String filter){

//        TrafficScotlandAPIController apiController = new TrafficScotlandAPIController();
//        if(filter.equals("Roadwork")){
//            TrafficScotlandAPIController controller = new TrafficScotlandAPIController();
//            TrafficScotlandSourceViewRequest request = TrafficScotlandSourceViewRequest.Today;
//            controller.getRoadWorks(request, this);
//        } else {
//            TrafficScotlandAPIController controller = new TrafficScotlandAPIController();
//            TrafficScotlandSourceViewRequest request = TrafficScotlandSourceViewRequest.Today;
//            controller.getCurrentIncidents(request, this);
//        }
    }

    @Override
    public void processFinish(TrafficScotlandAPIModel output) {
//        for (TrafficScotlandChannelItem item : output.getChannel().getChannelItems()) {
//            LatLng itemLatLong = item.getCoordinates();
//            if (SphericalUtil.computeDistanceBetween(item.getCoordinates(), searchedLatLong)<10000) {
//                map.addMarker(new MarkerOptions().position(item.getCoordinates())
//                        .title(item.getTitle()));
//            }
//        }
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = map.addPolyline((PolylineOptions) values[0]);
    }
    // endregion
}
