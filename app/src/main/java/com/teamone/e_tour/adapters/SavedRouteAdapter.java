package com.teamone.e_tour.adapters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.teamone.e_tour.R;
import com.teamone.e_tour.api.ticket.ViewBookedTicketApi;
import com.teamone.e_tour.entities.Image;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.models.BookedTicketManager;
import com.teamone.e_tour.utils.Formatter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class SavedRouteAdapter extends RecyclerView.Adapter<SavedRouteAdapter.ViewHolder> {

    Fragment context;
    private ArrayList<TouristRoute> routes = new ArrayList<>();

    public void setRoutes(ArrayList<TouristRoute> routes) {
        this.routes = routes;
        this.routes.sort(new Comparator<TouristRoute>() {
            @Override
            public int compare(TouristRoute o1, TouristRoute o2) {
                return o1.getCreatedAt().before(o2.getCreatedAt()) ? 1 : -1;
            }
        });
        notifyDataSetChanged();
    }

    public SavedRouteAdapter(Fragment context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_route_preview_card_large, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TouristRoute route = routes.get(position);

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
        return routes.size();
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
            newPrice = itemView.findViewById(R.id.new_price);
            addToFavourite = itemView.findViewById(R.id.add_favourite_btn);
            cardImage = itemView.findViewById(R.id.card_image);
            card = itemView.findViewById(R.id.tourist_route_card);
            rating = itemView.findViewById(R.id.rating_bar);
            numReview = itemView.findViewById(R.id.num_review);
        }
    }
}
