package com.teamone.e_tour.adapters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.teamone.e_tour.R;
import com.teamone.e_tour.entities.Image;
import com.teamone.e_tour.entities.PaymentCard;
import com.teamone.e_tour.entities.Voucher;
import com.teamone.e_tour.utils.Formatter;

import java.util.ArrayList;

public class PaymentCardAdapter extends RecyclerView.Adapter<PaymentCardAdapter.ViewHolder> {
    private final Fragment context;
    private ArrayList<PaymentCard> cards = new ArrayList<>();

    public PaymentCardAdapter(Fragment context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCards(ArrayList<PaymentCard> cards) {
        this.cards = cards;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_preview, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PaymentCard card = cards.get(position);

        holder.cardNumber.setText(context.getString(R.string.card_number_format, card.cardNumber.substring(15, 18)));

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
            }
        });
    }

    @Override
    public int getItemCount() {
//        Toast.makeText(context.requireActivity(), String.valueOf(cards.size()), Toast.LENGTH_SHORT).show();
        return cards.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cardNumber;
        MaterialCardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardNumber = itemView.findViewById(R.id.card_number);
            card = itemView.findViewById(R.id.card);
        }
    }
}