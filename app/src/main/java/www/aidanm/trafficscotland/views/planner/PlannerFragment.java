package www.aidanm.trafficscotland.views.planner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import www.aidanm.trafficscotland.R;

public class PlannerFragment extends Fragment {

    private PlannerViewModel plannerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        plannerViewModel =
//                ViewModelProviders.of(this).get(PlannerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_journey_planner, container, false);
//        final TextView textView = root.findViewById(R.id.text_journey_planner);
//        plannerViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}