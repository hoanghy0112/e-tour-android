package com.teamone.e_tour.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.teamone.e_tour.R;
import com.teamone.e_tour.entities.Image;
import com.teamone.e_tour.entities.Voucher;
import com.teamone.e_tour.utils.Formatter;

import java.util.ArrayList;
import java.util.Objects;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.ViewHolder> {
    private final Fragment context;
    private ArrayList<Voucher> vouchers = new ArrayList<>();
    private int cardId;
    IOnclick onClickHandler;

    public interface IOnclick {
        void onClick(Voucher voucher);
    }

    public VoucherAdapter(Fragment context) {
        this.context = context;
        this.cardId = R.layout.item_voucher_preview;
    }

    public VoucherAdapter(Fragment context, int cardId) {
        this.context = context;
        this.cardId = cardId;
    }

    public VoucherAdapter(Fragment context, int cardId, IOnclick onClickHandler) {
        this.context = context;
        this.cardId = cardId;
        this.onClickHandler = onClickHandler;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setVouchers(ArrayList<Voucher> vouchers) {
        this.vouchers = vouchers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getContext()).inflate(cardId, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Voucher voucher = vouchers.get(position);
        Glide.with(context).load(new Image(voucher.getImage()).getImageUri()).into(holder.image);

        if (holder.name != null) {
            holder.name.setText(voucher.getName());
            if (voucher.getType().equals("money"))
                holder.info.setText(context.requireActivity().getString(R.string.discount_money_message, Formatter.toCurrency((long) voucher.getValue()), Formatter.toCurrency(voucher.getMin())));
            else if (voucher.getType().equals("percent"))
                holder.info.setText(context.requireActivity().getString(R.string.discount_percent_message, (int)(voucher.getValue() * 100) + "%", Formatter.toCurrency(voucher.getMin()), Formatter.toCurrency(voucher.getMax())));
            else
                holder.info.setText(context.requireActivity().getString(R.string.discount_percent_message, (int)(voucher.getValue() * 100) + "%", Formatter.toCurrency(voucher.getMin()), Formatter.toCurrency(voucher.getMax())));

            holder.expired.setText("EXP: " + Formatter.dateToShortDateWithoutHourString(voucher.getExpiredAt()));
        }

        if (onClickHandler != null) {
            holder.item.setOnClickListener(v -> onClickHandler.onClick(voucher));
        } else {
            holder.item.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("id", voucher.get_id());
                bundle.putString("image", voucher.getImage());
                bundle.putString("backgroundImage", voucher.getBackgroundImage());
                bundle.putString("name", voucher.getName());
                bundle.putString("description", voucher.getDescription());
                bundle.putString("usingCondition", voucher.getUsingCondition());
                bundle.putString("type", voucher.getType());
                bundle.putString("companyId", voucher.getCompanyId()._id);
                bundle.putString("companyName", voucher.getCompanyId().name);
                bundle.putString("companyImage", voucher.getCompanyId().image);
                bundle.putString("companyEmail", voucher.getCompanyId().email);
                bundle.putString("companyId", voucher.getCompanyId()._id);
                bundle.putFloat("value", voucher.getValue());
                bundle.putFloat("min", voucher.getMin());
                bundle.putFloat("max", voucher.getMax());
                bundle.putString("expiredAt", Formatter.dateToDateWithoutHourString(voucher.getExpiredAt()));
                Navigation.findNavController(context.requireActivity(), R.id.home_wrapper).navigate(R.id.voucherDetailFragment, bundle);
            });
        }
    }

    @Override
    public int getItemCount() {
        return vouchers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView info;
        TextView expired;
        MaterialCardView item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.voucher_image);
            item = itemView.findViewById(R.id.voucher_item);
            name = itemView.findViewById(R.id.voucher_name);
            info = itemView.findViewById(R.id.voucher_info);
            expired = itemView.findViewById(R.id.voucher_expired_date);
        }
    }
}
