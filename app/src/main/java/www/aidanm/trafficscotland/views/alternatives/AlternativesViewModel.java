package www.aidanm.trafficscotland.views.alternatives;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AlternativesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AlternativesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is alternatives fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}