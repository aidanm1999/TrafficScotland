package www.aidanm.trafficscotland.controllers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.LinearLayout.LayoutParams;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import www.aidanm.trafficscotland.R;
import www.aidanm.trafficscotland.api.controllers.TrafficScotlandAPIController;
import www.aidanm.trafficscotland.controllers.helpers.DatePickerHelper;
import www.aidanm.trafficscotland.models.apimodels.TrafficScotlandAPIModel;
import www.aidanm.trafficscotland.models.apimodels.TrafficScotlandChannel;
import www.aidanm.trafficscotland.models.apimodels.TrafficScotlandChannelItem;
import www.aidanm.trafficscotland.models.enums.AsyncTaskCallUrlType;
import www.aidanm.trafficscotland.models.enums.TrafficScotlandSourceViewRequest;
import www.aidanm.trafficscotland.models.interfaces.AsyncResponse;

public class TodayController extends Fragment implements AsyncResponse {
    private TextInputEditText dateInput;
    private TextInputLayout dateInputLayout;
    private ConstraintLayout constraintLayout;
    private RecyclerView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    private ListView listView;
    private TodayController todayController = this;
    private String selectedDateString;
    private ArrayList<TrafficScotlandChannelItem> requestModels = new ArrayList<>();




    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {



        // region Call Traffic Scotland Controller to get Current Incidents
        TrafficScotlandAPIController controller = new TrafficScotlandAPIController();
        TrafficScotlandSourceViewRequest request = TrafficScotlandSourceViewRequest.Today;
        controller.getCurrentIncidents(request, this);
        controller.getRoadWorks(request, this);


        // endregion

        // region Find all Views By Id
        View root = inflater.inflate(R.layout.fragment_today, container, false);
        //list = root.findViewById(R.id.today_recycler_view);
        dateInput = root.findViewById(R.id.today_date_field);
        dateInputLayout = root.findViewById(R.id.today_date_layout);
        constraintLayout = root.findViewById(R.id.today_constraint_layout);
        listView = root.findViewById(R.id.today_list_view);
        // endregion

        // region Date Picker Instantiation
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
                    selectedDateString = dp.getHeaderText();
                    dateInput.setText(selectedDateString);

                    TrafficScotlandAPIController controller = new TrafficScotlandAPIController();
                    TrafficScotlandSourceViewRequest viewRequest = TrafficScotlandSourceViewRequest.Today;
                    String dateIn = dateInput.getText().toString();
                    String today = dpHelper.today();
                    if (!dateIn.equals(today)) {
                        // Any future dates
                        controller.getPlannedRoadWorks(viewRequest, todayController);
                    } else {
                        // Today
                        controller.getCurrentIncidents(viewRequest, todayController);
                        controller.getRoadWorks(viewRequest, todayController);

                    }
                } else {
                    Toast toast = Toast.makeText(getContext(),
                            "Please don't select a date prior to today",
                            Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });
        // endregion






        return root;


    }



    @Override
    public void processFinish(TrafficScotlandAPIModel output) {

        for(TrafficScotlandChannelItem item : output.getChannel().getChannelItems()){
            item.setType(output.getInput().getUrlType());
            requestModels.add(item);
        }



        if(output.getInput().getUrlType() != AsyncTaskCallUrlType.TrafficScotland_CurrentIncidents){
            // If all requests have been completed, display all requests to the view
            // There can be a max of 2 requests, one from the output and one from the tempModel

            ListAdapter adapter = new ListAdapter(this.getContext(), requestModels);
            listView.setAdapter(adapter);
            requestModels = new ArrayList<>();
        }


    }


    class ListAdapter extends ArrayAdapter<TrafficScotlandChannelItem> {

        private Context context;
        private List<TrafficScotlandChannelItem> allItems;

        ListAdapter (Context c, ArrayList<TrafficScotlandChannelItem> allItems) {
            super(c, R.layout.layout_today_card, R.id.textView1, allItems);
            this.context = c;
            this.allItems = allItems;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.layout_today_card, parent, false);
            ImageView currentImage = row.findViewById(R.id.image);
            TextView currentTitle = row.findViewById(R.id.textView1);
            TextView currentDescription = row.findViewById(R.id.textView2);
            TextView currentTypeText = row.findViewById(R.id.today_card_type_text);

            if(allItems.get(position).getType() == AsyncTaskCallUrlType.TrafficScotland_CurrentIncidents){
                currentImage.setImageResource(R.drawable.ic_car_crash_solid);
                currentTypeText.setText("Incident");
            } else if(allItems.get(position).getType() == AsyncTaskCallUrlType.TrafficScotland_Roadworks) {
                currentImage.setImageResource(R.drawable.ic_snowplow_solid);
                currentTypeText.setText("Roadwork");
            } else {
                currentImage.setImageResource(R.drawable.ic_calendar_check_regular);
                currentTypeText.setText("Planned");
            }

            currentTitle.setText(allItems.get(position).getTitle());
            currentDescription.setText(allItems.get(position).getDescription());


            return row;
        }
    }
}