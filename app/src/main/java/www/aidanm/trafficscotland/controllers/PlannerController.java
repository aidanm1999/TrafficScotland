package www.aidanm.trafficscotland.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import www.aidanm.trafficscotland.R;
import www.aidanm.trafficscotland.models.viewmodels.planner.PlannerViewModel;

public class PlannerController extends Fragment {

    private PlannerViewModel plannerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_planner, container, false);
        return root;
    }
}
