package com.teamone.e_tour.viewmodels;

import android.content.Context;
import android.content.Intent;

import androidx.databinding.ObservableField;

public class RegistrationViewModel {
    public final ObservableField<String> username = new ObservableField<>();
    public final ObservableField<String> password = new ObservableField<>();
    public final ObservableField<String> confirmedPassword = new ObservableField<>();
    public final ObservableField<String> fullname = new ObservableField<>();
    public final ObservableField<String> email = new ObservableField<>();
    public final ObservableField<String> phoneNumber = new ObservableField<>();
    public final ObservableField<String> address = new ObservableField<>();
    public final ObservableField<Boolean> isForeigner = new ObservableField<>(false);
    public final ObservableField<String> id = new ObservableField<>();

    private Context context;

    public RegistrationViewModel(Context context) {
        this.context = context;
    }

    public void onClickNext() {
    }

    public void onClickSubmit() {

    }
}
