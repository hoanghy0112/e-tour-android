package com.teamone.e_tour.adapters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.teamone.e_tour.R;
import com.teamone.e_tour.entities.Image;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.utils.Formatter;

import java.util.ArrayList;

public class RecommendedRouteListAdapter extends RecyclerView.Adapter<RecommendedRouteListAdapter.ViewHolder> {
    Fragment context;
    int cardId;
    private ArrayList<TouristRoute> routeList = new ArrayList<>();

    public ArrayList<TouristRoute> getRouteList() {
        return routeList;
    }

    public void setRouteList(ArrayList<TouristRoute> routeList) {
        this.routeList = routeList;
        notifyDataSetChanged();
    }

    public RecommendedRouteListAdapter(ArrayList<TouristRoute> routeList, Fragment context, int cardId) {
        this.routeList = routeList;
        this.context = context;
        this.cardId = cardId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(cardId, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TouristRoute route = routeList.get(position);

        holder.name.setText(route.getName());
        holder.newPrice.setText(Formatter.toCurrency(route.getReservationFee()));
        holder.rating.setRating(route.getRate());
        holder.numReview.setText(route.getNum() + " " + context.getString(R.string.reviews));

        if (route.getImages().size() > 0)
            Glide.with(context).load(new Image(route.getImages().get(0)).getImageUri()).into(holder.cardImage);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", route.get_id());
                NavHostFragment.findNavController(context).navigate(R.id.detailTourFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView oldPrice;
        TextView newPrice;
        ImageView addToFavourite;
        ImageView cardImage;
        MaterialCardView card;
        RatingBar rating;
        TextView numReview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
//            oldPrice = itemView.findViewById(R.id.old_price);
            newPrice = itemView.findViewById(R.id.new_price);
            addToFavourite = itemView.findViewById(R.id.add_favourite_btn);
            cardImage = itemView.findViewById(R.id.card_image);
            card = itemView.findViewById(R.id.tourist_route_card);
            rating = itemView.findViewById(R.id.rating_bar);
            numReview = itemView.findViewById(R.id.num_review);
        }
    }
}
