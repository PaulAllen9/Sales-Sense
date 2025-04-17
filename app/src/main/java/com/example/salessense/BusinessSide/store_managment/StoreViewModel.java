package com.example.salessense.BusinessSide.store_managment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StoreViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public StoreViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("The store is empty. Please try adding a product!");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public void updateText(String newText) {
        mText.setValue(newText);
    }

}