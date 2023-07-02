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
    private ArrayList<TouristRoute> routeList;

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

    @Override
    public int getItemViewType(int position) {
        if (routeList.size() == 0)
            return 1;
        return 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == 0)
            view = LayoutInflater.from(parent.getContext()).inflate(cardId, parent, false);
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.not_found, parent, false);
            ((TextView) view.findViewById(R.id.loading_text)).setText(R.string.not_found);
        }

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            return;
        }

        TouristRoute route = routeList.get(position);

        if (route.getType() == null || route.getType().equals("country")) {
//            holder.routeType.setText(R.string.domestic);
//            holder.routeType.setTextColor(context.getActivity().getColor(R.color.white));
//            holder.routeType.setBackground(context.getActivity().getDrawable(R.drawable.route_type_domestic));
        } else if (route.getType().equals("foreign")) {
//            holder.routeType.setText(R.string.international);
//            holder.routeType.setTextColor(context.getActivity().getColor(R.color.black));
//            holder.routeType.setBackground(context.getActivity().getDrawable(R.drawable.route_type_international));
        }

        if (holder.companyName != null) {
            holder.companyName.setText(route.getCompany().name);
        }

        holder.name.setText(route.getName());
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

//        SavedRouteManager.getInstance((AppCompatActivity) context.getActivity()).getRoutes().observe(context.getViewLifecycleOwner(), new Observer<ArrayList<TouristRoute>>() {
//            @Override
//            public void onChanged(ArrayList<TouristRoute> touristRoutes) {
//                if (touristRoutes == null) return;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if (routeList == null) return 0;
        if (routeList.size() == 0) return 1;
        return routeList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView companyName;
        ImageView cardImage;
        MaterialCardView card;
        RatingBar rating;
        TextView numReview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            companyName = itemView.findViewById(R.id.company_name);
            cardImage = itemView.findViewById(R.id.card_image);
            card = itemView.findViewById(R.id.tourist_route_card);
            rating = itemView.findViewById(R.id.rating_bar);
            numReview = itemView.findViewById(R.id.num_review);
        }
    }
}
