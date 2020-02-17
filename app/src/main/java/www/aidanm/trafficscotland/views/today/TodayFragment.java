package www.aidanm.trafficscotland.views.today;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.TimeZone;

import www.aidanm.trafficscotland.R;
import www.aidanm.trafficscotland.controllers.helpers.DatePickerHelper;

public class TodayFragment extends Fragment {

    private TodayViewModel todayViewModel;
    private TextInputEditText dateInput;

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



        dateInput = root.findViewById(R.id.today_in_transit_date_field);
        final DatePickerHelper dpHelper = new DatePickerHelper(dateInput);
        final MaterialDatePicker dp = dpHelper.build();
        dateInput.setText(dpHelper.today());



        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dp.show(getFragmentManager(), "DATE_PICKER");
            }
        });



        dp.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                if(dpHelper.validate(Long.parseLong(selection.toString())))
                {
                    dateInput.setText(dp.getHeaderText());
                } else {
                    Toast toast = Toast.makeText(getContext(),
                            "Please don't select a date prior to today",
                            Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });

        return root;
    }
}