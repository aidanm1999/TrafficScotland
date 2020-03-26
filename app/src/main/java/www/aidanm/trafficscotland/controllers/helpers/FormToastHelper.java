package www.aidanm.trafficscotland.controllers.helpers;

// Developer: Aidan Marshall
// Student ID: S1828601

import android.content.Context;
import android.widget.Toast;

public class FormToastHelper {
    private Context context;

    public FormToastHelper(Context ctxt){
        context = ctxt;
    }


    public void basicInputError(){
        Toast toast = Toast.makeText(context.getApplicationContext(), "An input you have entered is wrong", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void inputError(String location){
        Toast toast = Toast.makeText(context.getApplicationContext(), "Bad input in the "+location+" Field", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void custom(String message){
        Toast toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
