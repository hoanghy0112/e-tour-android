package com.teamone.e_tour.dialogs;

import static com.teamone.e_tour.R.color.transparent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.teamone.e_tour.R;
import com.teamone.e_tour.activities.AuthenticationActivity;
import com.teamone.e_tour.databinding.DialogLoadingBinding;

public class LoadingDialog extends Dialog {
    DialogLoadingBinding binding;
    Context context;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        this.setCancelable(false);
        this.context = context;
        binding = DialogLoadingBinding.inflate(((Activity) context).getLayoutInflater());
        setContentView(binding.getRoot());

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.80);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(transparent, context.getTheme())));
    }

    public Dialog showLoading(String text) {
        if (isShowing()) return this;
        this.setCancelable(false);
        binding.loginDialogProgress.setVisibility(View.VISIBLE);
        binding.loginDialogMessage.setText(text);
        show();
        return this;
    }

    public Dialog showLoading() {
        if (isShowing()) return this;
        this.setCancelable(false);
        binding.loginDialogProgress.setVisibility(View.VISIBLE);
        binding.loginDialogMessage.setText(context.getResources().getText(R.string.please_wait_we_are_signing_in_for_you));
        show();
        return this;
    }

    public Dialog showError(String error) {
        if (isShowing()) dismiss();
        this.setCancelable(true);
        binding.getRoot().removeView(binding.loginDialogProgress);
        binding.loginDialogMessage.setText(error);
        show();
        return this;
    }
}
