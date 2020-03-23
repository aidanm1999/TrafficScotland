package www.aidanm.trafficscotland.controllers.helpers;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class HandlePlaceSearchToMap {
    private boolean failedValidation;
    private GoogleMap map;
    private int ZOOM_LEVEL = 12;


    public void validateInputs(List inputs){

    }

    private void updateMapCamera(LatLng latLngIn){
        map.clear();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngIn, ZOOM_LEVEL));
    }

    private void addCircle(LatLng latLngIn){
        map.addCircle(new CircleOptions()
                .center(latLngIn)
                .radius(10000)
                .strokeColor(Color.rgb(14,21,58)));
    }

    public void submitForm(){
        failedValidation = false;
    }
}
