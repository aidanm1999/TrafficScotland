package www.aidanm.trafficscotland.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;

import www.aidanm.trafficscotland.R;
import www.aidanm.trafficscotland.api.controllers.TrafficScotlandAPIController;
import www.aidanm.trafficscotland.api.controllers.TransportAPIController;
import www.aidanm.trafficscotland.api.pullparsers.TrafficScotlandPullParser;
import www.aidanm.trafficscotland.api.tasks.HttpGetRequest;
import www.aidanm.trafficscotland.controllers.helpers.DatePickerHelper;
import www.aidanm.trafficscotland.models.apimodels.AsyncTaskCallInput;
import www.aidanm.trafficscotland.models.enums.AsyncTaskCallUrlType;
import www.aidanm.trafficscotland.models.viewmodels.today.TodayViewModel;

public class TodayController extends Fragment {
    private TodayViewModel todayViewModel;
    private TextInputEditText dateInput;
    private EditText editTxt;
    private Button btn;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        TrafficScotlandAPIController controller = new TrafficScotlandAPIController();
        controller.getCurrentIncidents();
//        // TODO - This should be called in the api controller
//        TrafficScotlandPullParser pp = new TrafficScotlandPullParser();
//        pp.execute();

        View root = inflater.inflate(R.layout.fragment_today, container, false);
        dateInput = root.findViewById(R.id.today_date_field);
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
                if (dpHelper.validate(Long.parseLong(selection.toString()))) {
                    dateInput.setText(dp.getHeaderText());
                } else {
                    Toast toast = Toast.makeText(getContext(),
                            "Please don't select a date prior to today",
                            Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });


//        editTxt = (EditText) root.findViewById(R.id.editText);
//        btn = (Button) root.findViewById(R.id.button);
//        list = (ListView) root.findViewById(R.id.today_results_list);
//        arrayList = new ArrayList<String>(
//                Arrays.asList("Item 1",
//                        "Item 2",
//                        "Item 3"));


//        adapter = new ArrayAdapter<String>(getContext().getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
//
//        list.setAdapter(adapter);
        return root;


    }
}