package www.aidanm.trafficscotland.views.alternatives;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import www.aidanm.trafficscotland.R;

public class AlternativesFragment extends Fragment {

    private AlternativesViewModel alternativesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        alternativesViewModel =
//                ViewModelProviders.of(this).get(AlternativesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_alternatives, container, false);
//        final TextView textView = root.findViewById(R.id.text_have_a_look);
//        alternativesViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}