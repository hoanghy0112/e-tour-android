package com.teamone.e_tour.dialogs;

import static com.teamone.e_tour.R.color.transparent;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.teamone.e_tour.R;
import com.teamone.e_tour.activities.RegistrationActivity;
import com.teamone.e_tour.databinding.DialogRegistrationBinding;

public class RegistrationDialog extends Dialog {
    DialogRegistrationBinding binding;
    Context context;

    public RegistrationDialog(@NonNull Context context) {
        super(context);
        this.setCancelable(true);
        this.context = context;
        binding = DialogRegistrationBinding.inflate(((RegistrationActivity) context).getLayoutInflater());
        setContentView(binding.getRoot());

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.80);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(transparent, context.getTheme())));
    }


    public void showLoading() {
        if (isShowing()) return;
        binding.signupDialogProgress.setVisibility(View.VISIBLE);
        binding.signupDialogMessage.setText(context.getResources().getText(R.string.please_wait_while_we_are_signing_up_for_you));
        show();
    }

    public void showError(String error) {
        if (isShowing()) dismiss();
        binding.getRoot().removeView(binding.signupDialogProgress);
        binding.signupDialogMessage.setText(context.getResources().getText(R.string.failed_to_sign_up).toString() + "\nError message: " + error);
        show();
    }
}

