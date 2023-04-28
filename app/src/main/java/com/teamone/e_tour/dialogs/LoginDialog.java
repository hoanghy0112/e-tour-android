package com.teamone.e_tour.dialogs;

import static com.teamone.e_tour.R.color.transparent;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.teamone.e_tour.R;
import com.teamone.e_tour.activities.AuthenticationActivity;
import com.teamone.e_tour.databinding.DialogLoginBinding;

import java.util.Objects;

public class LoginDialog extends Dialog {
    DialogLoginBinding binding;
    Context context;

    public LoginDialog(@NonNull Context context) {
        super(context);
        this.setCancelable(true);
        this.context = context;
        binding = DialogLoginBinding.inflate(((AuthenticationActivity) context).getLayoutInflater());
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
        binding.loginDialogProgress.setVisibility(View.VISIBLE);
        binding.loginDialogMessage.setText(context.getResources().getText(R.string.please_wait_we_are_signing_in_for_you));
        show();
    }

    public void showError(String error) {
        if (isShowing()) dismiss();
        binding.getRoot().removeView(binding.loginDialogProgress);
        binding.loginDialogMessage.setText(context.getResources().getText(R.string.fail_to_sign_in).toString() + "\nError message: " + error);
        show();
    }
}
