package com.teamone.e_tour.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.teamone.e_tour.R;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.fragments.HomeFragment;

import java.util.ArrayList;

public class RecommendedRouteListAdapter extends RecyclerView.Adapter<RecommendedRouteListAdapter.ViewHolder> {
    Fragment context;
    private ArrayList<TouristRoute> routeList = new ArrayList<>();

    public ArrayList<TouristRoute> getRouteList() {
        return routeList;
    }

    public void setRouteList(ArrayList<TouristRoute> routeList) {
        this.routeList = routeList;
        notifyDataSetChanged();
    }

    public RecommendedRouteListAdapter(ArrayList<TouristRoute> routeList, Fragment context) {
        this.routeList = routeList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_tour_preview_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TouristRoute route = routeList.get(position);

        holder.name.setText(route.getName());
        holder.newPrice.setText(route.getReservationFee().toString());
        holder.oldPrice.setText(route.getImages().toString());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(context).navigate(R.id.action_homeFragment_to_detailTourFragment);
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
        MaterialCardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            oldPrice = itemView.findViewById(R.id.old_price);
            newPrice = itemView.findViewById(R.id.new_price);
            addToFavourite = itemView.findViewById(R.id.add_favourite_btn);
            card = itemView.findViewById(R.id.tourist_route_card);
        }
    }
}
