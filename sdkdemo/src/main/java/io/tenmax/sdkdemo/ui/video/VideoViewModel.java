package io.tenmax.sdkdemo.ui.video;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VideoViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public VideoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is video fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}



