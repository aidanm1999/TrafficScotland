package www.aidanm.trafficscotland.controllers;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import www.aidanm.trafficscotland.R;
import www.aidanm.trafficscotland.api.controllers.TrafficScotlandAPIController;
import www.aidanm.trafficscotland.api.controllers.TransportAPIController;
import www.aidanm.trafficscotland.controllers.helpers.FormToastHelper;
import www.aidanm.trafficscotland.models.apimodels.TrafficScotlandAPIModel;
import www.aidanm.trafficscotland.models.apimodels.TrafficScotlandChannelItem;
import www.aidanm.trafficscotland.models.enums.TrafficScotlandSourceViewRequest;
import www.aidanm.trafficscotland.models.interfaces.AsyncResponse;

public class LookController extends Fragment implements OnMapReadyCallback, AsyncResponse {

    // region Fields
    private View root;
    private GoogleMap map;
    private MapView mapView;
    private TextInputEditText searchForRoadText;
    private List<AutocompletePrediction> predictionList;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    private AutoCompleteTextView filterText;
    private Button submitButton;
    private TextInputLayout filterLayout;
    private TextInputLayout searchForRoadLayout;
    private FormToastHelper toastHelper;
    private LatLng searchedLatLong;

    private int ZOOM_LEVEL = 12;
    // endregion

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // region Find all Views By Id
        root = inflater.inflate(R.layout.fragment_look, container, false);
        filterLayout = root.findViewById(R.id.look_filter_layout);
        filterText = root.findViewById(R.id.look_filter_field);
        searchForRoadLayout = root.findViewById(R.id.look_search_layout);
        searchForRoadText = root.findViewById(R.id.look_search_field);
        submitButton = root.findViewById(R.id.look_search_submit);
        // endregion

        //region Instantiate - Dropdown Box Filter By
        String[] filterArray = new String[] {"Roadwork", "Current Incident"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.dropdown_menu_popup_item,
                        filterArray);

        filterText.setAdapter(adapter);
        //endregion

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
        mapView = root.findViewById(R.id.look_map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync((OnMapReadyCallback) this);
        // endregion

        // region Instantiate - FormToastHelper
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

        // region Form Validation
        boolean failedValidation = false;

        if(filterText.getText().toString() == null || filterText.getText().toString().equals("")){
            toastHelper.inputError("Filter");
            failedValidation = true;
        }

        if(searchForRoadText.getText().toString() == null || searchForRoadText.getText().toString().equals("")){
            toastHelper.inputError("Search");
            failedValidation = true;
        }
        // endregion

        if(!failedValidation){
            searchForRoadText.onEditorAction(EditorInfo.IME_ACTION_DONE);

            findPlace();
        }

    }

    private void findPlace(){
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        final FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest
                .builder()
                .setCountry("GB")
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(searchForRoadText.getText().toString() + " Scotland")
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
                                    Log.i("api-response", fetchPlaceResponse.getPlace().getName());

                                    searchedLatLong = fetchPlaceResponse.getPlace().getLatLng();
                                    if (searchedLatLong != null) {
                                        updateMapCamera();
                                        callTrafficScotlandApi(filterText.getText().toString());
                                    }
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
    }


    private void updateMapCamera(){
        map.clear();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(searchedLatLong, ZOOM_LEVEL));
        map.addCircle(new CircleOptions()
                .center(searchedLatLong)
                .radius(10000)
                .strokeColor(Color.rgb(14,21,58)));
    }

    private void callTrafficScotlandApi(String filter){

        if(filter.equals("Roadwork")){
            TrafficScotlandAPIController controller = new TrafficScotlandAPIController();
            TrafficScotlandSourceViewRequest request = TrafficScotlandSourceViewRequest.Look;
            controller.getRoadWorks(request, this);
        } else {
            TrafficScotlandAPIController controller = new TrafficScotlandAPIController();
            TrafficScotlandSourceViewRequest request = TrafficScotlandSourceViewRequest.Look;
            controller.getCurrentIncidents(request, this);
        }
    }


    @Override
    public void processFinish(TrafficScotlandAPIModel output) {
        for (TrafficScotlandChannelItem item : output.getChannel().getChannelItems()) {
            if (SphericalUtil.computeDistanceBetween(item.getCoordinates(), searchedLatLong)<10000) {
                map.addMarker(new MarkerOptions().position(item.getCoordinates())
                        .title(item.getTitle()));
            }
        }
    }
    // endregion
}

