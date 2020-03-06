package www.aidanm.trafficscotland.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import www.aidanm.trafficscotland.R;

public class LookController extends Fragment implements OnMapReadyCallback {
    private View rootView;
    private GoogleMap map;
    private MapView mapView;
    MarkerOptions place1, place2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //region Text editor code, irrelevant to maps
        View root = inflater.inflate(R.layout.fragment_look, container, false);

        String[] countries = new String[] {"Roadwork", "Current Incident"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.dropdown_menu_popup_item,
                        countries);

        AutoCompleteTextView editTextFilledExposedDropdown = root.findViewById(R.id.look_filter_field);
        editTextFilledExposedDropdown.setAdapter(adapter);
        //endregion

        MapsInitializer.initialize(this.getActivity());
        mapView = root.findViewById(R.id.look_map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync((OnMapReadyCallback) this);

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(52.930535,-0.046874)));
    }

//    @Override
//    public void onResume() {
//        mapView.onResume();
//        super.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        mapView.onPause();
//        super.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        mapView.onDestroy();
//        super.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        mapView.onLowMemory();
//        super.onLowMemory();
//    }
}
