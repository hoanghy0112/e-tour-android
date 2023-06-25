package com.teamone.e_tour.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.teamone.e_tour.R;
import com.teamone.e_tour.entities.Image;
import com.teamone.e_tour.entities.Voucher;
import com.teamone.e_tour.utils.Formatter;

import java.util.ArrayList;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.ViewHolder> {
    private Fragment context;
    private ArrayList<Voucher> vouchers = new ArrayList<>();

    public VoucherAdapter(Fragment context) {
        this.context = context;
    }

    public void setVouchers(ArrayList<Voucher> vouchers) {
        this.vouchers = vouchers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getContext()).inflate(R.layout.item_voucher_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Voucher voucher = vouchers.get(position);
        Glide.with(context).load(new Image(voucher.getImage()).getImageUri()).into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", voucher.get_id());
                bundle.putString("image", voucher.getImage());
                bundle.putString("name", voucher.getName());
                bundle.putString("description", voucher.getDescription());
                bundle.putString("usingCondition", voucher.getUsingCondition());
                bundle.putString("companyId", voucher.getCompanyId()._id);
                bundle.putFloat("value", voucher.getValue());
                bundle.putString("expiredAt", Formatter.dateToDateWithHourString(voucher.getExpiredAt()));
                Navigation.findNavController(context.getActivity(), R.id.home_wrapper).navigate(R.id.voucherDetailFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vouchers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.voucher_image);
        }
    }
}
