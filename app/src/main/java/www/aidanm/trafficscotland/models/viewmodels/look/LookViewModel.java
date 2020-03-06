package www.aidanm.trafficscotland.models.viewmodels.look;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LookViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LookViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is look fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}