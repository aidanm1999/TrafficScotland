package www.aidanm.trafficscotland.controllers;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import www.aidanm.trafficscotland.R;

public class LookController extends Fragment implements OnMapReadyCallback {
    private View rootView;
    private GoogleMap map;
    private MapView mapView;
    private MarkerOptions place1, place2;
    private TextInputEditText searchForRoadEditText;
    private List<AutocompletePrediction> predictionList;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_look, container, false);

        //region Dropdown Box
        String[] filterArray = new String[] {"Roadwork", "Current Incident"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.dropdown_menu_popup_item,
                        filterArray);

        AutoCompleteTextView editTextFilledExposedDropdown = rootView.findViewById(R.id.look_filter_field);
        editTextFilledExposedDropdown.setAdapter(adapter);
        //endregion

        // region SearchForRoadEditText
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        Places.initialize(getContext(), "AIzaSyB-wEH8r3o-6Ajme-I33bBUGx-9wuxGgyI");
        placesClient = Places.createClient(getContext());
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        searchForRoadEditText = rootView.findViewById(R.id.look_search_field);
        searchForRoadEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest
                        .builder()
                        .setCountry("GB")
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setSessionToken(token)
                        .setQuery(s.toString())
                        .build();

                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
                        if(task.isSuccessful()){
                            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
                            if(predictionsResponse != null){
                                predictionList = predictionsResponse.getAutocompletePredictions();
                                List<String> suggestionList = new ArrayList<>();
                                for(int i=0; i<predictionList.size();i++){
                                    AutocompletePrediction prediction = predictionList.get(i);
                                    suggestionList.add(prediction.getFullText(null).toString());
                                }

                                Log.i("tag", suggestionList.toString());

                                searchForRoadEditText.setHint(suggestionList.size());//.updateLastSuggestions(suggestionList);
//                                if(!materialSearchBar.isSuggestionsVisible()){
//                                    materialSearchBar.showSuggestionsList();
//                                }
                            }
                        } else {
                            Log.i("mytag", "prediction fetching task unsuccessful");
                        }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // endregion

        // region Maps Initializer
        MapsInitializer.initialize(this.getActivity());
        mapView = rootView.findViewById(R.id.look_map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync((OnMapReadyCallback) this);
        // endregion

        return rootView;
    }

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
}
