package www.aidanm.trafficscotland.controllers;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import www.aidanm.trafficscotland.R;
import www.aidanm.trafficscotland.api.controllers.TrafficScotlandAPIController;
import www.aidanm.trafficscotland.controllers.helpers.DatePickerHelper;
import www.aidanm.trafficscotland.models.apimodels.TrafficScotlandAPIModel;
import www.aidanm.trafficscotland.models.apimodels.TrafficScotlandChannelItem;
import www.aidanm.trafficscotland.models.enums.TrafficScotlandSourceViewRequest;
import www.aidanm.trafficscotland.models.interfaces.AsyncResponse;
import www.aidanm.trafficscotland.models.viewmodels.today.TodayViewModel;

public class TodayController extends Fragment implements AsyncResponse {
    private TextInputEditText dateInput;
    private TextInputLayout dateInputLayout;
    private ConstraintLayout constraintLayout;
    private RecyclerView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // region Call Traffic Scotland Controller to get Current Incidents
        TrafficScotlandAPIController controller = new TrafficScotlandAPIController();
        TrafficScotlandSourceViewRequest request = TrafficScotlandSourceViewRequest.Today;
        controller.getCurrentIncidents(request, this);

        // endregion

        // region Find all Views By Id
        View root = inflater.inflate(R.layout.fragment_today, container, false);
        list = root.findViewById(R.id.today_recycler_view);
        dateInput = root.findViewById(R.id.today_date_field);
        dateInputLayout = root.findViewById(R.id.today_date_layout);
        constraintLayout = root.findViewById(R.id.today_constraint_layout);
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
                    dateInput.setText(dp.getHeaderText());
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
        for (TrafficScotlandChannelItem item : output.getChannel().getChannelItems()) {
            addMaterialCardView(item);
        }
    }




    public void addMaterialCardView(TrafficScotlandChannelItem item){

        Log.i("ItemPassedToView",item.getTitle());

//        materialCardView = new MaterialCardView(getContext());
//
//        layoutparams = new LayoutParams(LayoutParams.MATCH_PARENT, 200);
//        materialCardView.setLayoutParams(layoutparams);
//        materialCardView.setBackgroundResource(R.drawable.background1);






//
//
//        cardview = new CardView(getContext());
//
//        layoutparams = new LayoutParams(
//                LayoutParams.MATCH_PARENT,
//                200
//        );
//
//
//
//        //layoutparams.height = 300;
//
//        cardview.setLayoutParams(layoutparams);
//        cardview.setRadius(20);
//        int id=10001992;
//        cardview.setId(id);
//        //cardview.setId(View.generateViewId());
//        //cardview.setId(@+);
//        cardview.setBackgroundResource(R.drawable.background1);
        //cardview.setPadding(25, 25, 25, 25);
        //cardview.setMinimumHeight(00);

//        layoutparams = new LayoutParams(
//                LayoutParams.MATCH_PARENT,
//                LayoutParams.MATCH_PARENT
//        );
//
//       textview = new TextView(getContext());
//        textview.setLayoutParams(layoutparams);
//        textview.setText("CardView Programmatically");
//        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
//        textview.setTextColor(Color.WHITE);
//        textview.setPadding(25,25,25,25);
//        textview.setGravity(Gravity.CENTER);
//        cardview.addView(textview);
        //constraintLayout.addView(materialCardView);

//        ConstraintSet set = new ConstraintSet();
//        Log.i("tag", Integer.toString(cardview.getId()));
//        set.connect(cardview.getId(), ConstraintSet.TOP,
//                dateInputLayout.getId(), ConstraintSet.BOTTOM, 5);
//        set.applyTo(constraintLayout);
//
//
//
//        ConstraintSet set = new ConstraintSet();
//
//          ImageView view = new ImageView(this);
//          constraintLayout.addView(materialCardView);
//          set.clone(constraintLayout);
//          set.connect(materialCardView.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP, 60);
//          set.applyTo(constraintLayout);

    }
}