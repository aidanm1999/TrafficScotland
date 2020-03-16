package www.aidanm.trafficscotland.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import www.aidanm.trafficscotland.models.viewmodels.planner.PlannerViewModel;

public class PlannerController extends Fragment implements OnMapReadyCallback {

    private PlannerViewModel plannerViewModel;
    private View rootView;
    private GoogleMap map;
    private MapView mapView;
    private MarkerOptions place1, place2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_planner, container, false);


        MapsInitializer.initialize(this.getActivity());
        mapView = root.findViewById(R.id.planner_map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync((OnMapReadyCallback) this);

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng uk = new LatLng(54, -3);
        // map.addMarker(new MarkerOptions().position(uk).title("Marker in Sydney"));
        // map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));
        // map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));
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
