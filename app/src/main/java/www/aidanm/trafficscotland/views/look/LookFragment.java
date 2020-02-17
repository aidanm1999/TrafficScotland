package www.aidanm.trafficscotland.views.look;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import www.aidanm.trafficscotland.R;

public class LookFragment extends Fragment {

    private LookViewModel lookViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


//        lookViewModel =
//                ViewModelProviders.of(this).get(LookViewModel.class);
        View root = inflater.inflate(R.layout.fragment_have_a_look, container, false);
//        final TextView textView = root.findViewById(R.id.text_have_a_look);
//        lookViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        String[] COUNTRIES = new String[] {"Item 1", "Item 2", "Item 3", "Item 4"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.dropdown_menu_popup_item,
                        COUNTRIES);

        AutoCompleteTextView editTextFilledExposedDropdown =
                root.findViewById(R.id.have_a_look_filter_field);
        editTextFilledExposedDropdown.setAdapter(adapter);
        return root;
    }
}