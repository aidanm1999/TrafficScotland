package www.aidanm.trafficscotland.controllers;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



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
                    if (!dateInput.getText().toString().equals(dpHelper.today())) {

                        controller.getPlannedRoadWorks(viewRequest, todayController);
                    } else {

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
        // Todo - Add a circular progress indicator and remove when processFinish runs

        if(output.getInput().getUrlType() == AsyncTaskCallUrlType.TrafficScotland_PlannedRoadworks){
            ArrayList<TrafficScotlandChannelItem> tempChannelItems = new ArrayList<>();
            DatePickerHelper dpHelper = new DatePickerHelper(dateInput);
            for(TrafficScotlandChannelItem item : output.getChannel().getChannelItems()){
                String itemDate = dpHelper.formatDate(item.getDatePublished()); // TODO - It is not date published
                if(itemDate.toString().equals(selectedDateString.toString())) {
                    tempChannelItems.add(item);
                }
            }
            output.getChannel().setChannelItems(tempChannelItems);

        }

        MyAdapter adapter = new MyAdapter(this.getContext(), output);
        listView.setAdapter(adapter);
    }


    class MyAdapter extends ArrayAdapter<TrafficScotlandChannelItem> {

        private Context context;
        private TrafficScotlandAPIModel output;

        MyAdapter (Context c, TrafficScotlandAPIModel output) {
            super(c, R.layout.layout_today_card, R.id.textView1, output.getChannel().getChannelItems());
            this.context = c;
            this.output = output;
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

            if(output.getInput().getUrlType() == AsyncTaskCallUrlType.TrafficScotland_CurrentIncidents){
                currentImage.setImageResource(R.drawable.ic_car_crash_solid);
                currentTypeText.setText("Incident");
            } else if(output.getInput().getUrlType() == AsyncTaskCallUrlType.TrafficScotland_Roadworks) {
                currentImage.setImageResource(R.drawable.ic_snowplow_solid);
                currentTypeText.setText("Roadwork");
            } else {
                currentImage.setImageResource(R.drawable.ic_calendar_check_regular);
                currentTypeText.setText("Planned");
            }

            currentTitle.setText(output.getChannel().getChannelItems().get(position).getTitle());
            currentDescription.setText(output.getChannel().getChannelItems().get(position).getDescription());


            return row;
        }
    }
}