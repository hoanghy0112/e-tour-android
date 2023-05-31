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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.teamone.e_tour.R;
import com.teamone.e_tour.api.savedList.AddToSaveListApi;
import com.teamone.e_tour.api.savedList.RemoveFromSaveListApi;
import com.teamone.e_tour.constants.SocketMessage;
import com.teamone.e_tour.entities.Image;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.models.SavedRouteManager;
import com.teamone.e_tour.utils.Formatter;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.ViewHolder> {
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

    public RouteListAdapter(Fragment context, int cardId) {
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

        if (route.getType() == null || route.getType().equals("country")) {
            holder.routeType.setText(R.string.domestic);
            holder.routeType.setTextColor(context.getActivity().getColor(R.color.white));
            holder.routeType.setBackground(context.getActivity().getDrawable(R.drawable.route_type_domestic));
        } else if (route.getType().equals("foreign")) {
            holder.routeType.setText(R.string.international);
            holder.routeType.setTextColor(context.getActivity().getColor(R.color.black));
            holder.routeType.setBackground(context.getActivity().getDrawable(R.drawable.route_type_international));
        }

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

                JSONObject object = new JSONObject();
                try {
                    object.put("routeId", route.get_id());
                    object.put("point", 1);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                SocketManager.getInstance(context.getActivity()).emit(SocketMessage.Client.INCREASE_POINT, object);
            }
        });

        SavedRouteManager.getInstance((AppCompatActivity) context.getActivity()).getRoutes().observe(context.getViewLifecycleOwner(), new Observer<ArrayList<TouristRoute>>() {
            @Override
            public void onChanged(ArrayList<TouristRoute> touristRoutes) {
                if (touristRoutes == null) return;
                holder.addToFavourite.setCheckedState(MaterialCheckBox.STATE_UNCHECKED);
                touristRoutes.forEach(touristRoute -> {
                    if (touristRoute.get_id().equals(route.get_id())) {
                        holder.addToFavourite.setCheckedState(MaterialCheckBox.STATE_CHECKED);
                    }
                });
            }
        });

        holder.addToFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.addToFavourite.getCheckedState() == MaterialCheckBox.STATE_CHECKED) {
                    AddToSaveListApi.getInstance(context.getActivity()).send(route.get_id());
                }
                else {
                    RemoveFromSaveListApi.getInstance(context.getActivity()).send(route.get_id());
//                    SavedRouteManager.getInstance((AppCompatActivity) context.getActivity()).remove(route.get_id());
                    holder.addToFavourite.setChecked(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView routeType;
        TextView name;
        TextView oldPrice;
        TextView newPrice;
        MaterialCheckBox addToFavourite;
        ImageView cardImage;
        MaterialCardView card;
        RatingBar rating;
        TextView numReview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            routeType = itemView.findViewById(R.id.route_type);
            newPrice = itemView.findViewById(R.id.new_price);
            addToFavourite = itemView.findViewById(R.id.add_favourite_btn);
            cardImage = itemView.findViewById(R.id.card_image);
            card = itemView.findViewById(R.id.tourist_route_card);
            rating = itemView.findViewById(R.id.rating_bar);
            numReview = itemView.findViewById(R.id.num_review);
        }
    }
}
