package www.aidanm.trafficscotland.views.today;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import www.aidanm.trafficscotland.R;

public class TodayFragment extends Fragment {

    private TodayViewModel todayViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        todayViewModel =
//                ViewModelProviders.of(this).get(TodayViewModel.class);
        View root = inflater.inflate(R.layout.fragment_today_in_transit, container, false);
//        final TextView textView = root.findViewById(R.id.text_today_in_transit);
//        todayViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}