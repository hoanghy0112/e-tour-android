package com.teamone.e_tour.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.teamone.e_tour.R;
import com.teamone.e_tour.entities.Tour;

import java.util.ArrayList;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.ViewHolder> {
    private Fragment context;
    private String routeName = "";

    public TourAdapter(Fragment context) {
        this.context = context;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    private ArrayList<Tour> tourList = new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tour_preview_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tour tour = tourList.get(position);

        holder.name.setText(tour.getName());
        holder.from.setText(tour.getFrom().toString());
        holder.type.setText(tour.getType() == "normal" ? context.getString(R.string.normal_tour) : context.getString(R.string.promotion_tour));
        holder.price.setText("VND " + tour.getPrice().toString());

        holder.bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(context).navigate(R.id.action_tourListFragment_to_bookTicketFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public void setTourList(ArrayList<Tour> tourList) {
        this.tourList = tourList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView price;
        TextView from;
        TextView type;
        TextView name;
        Button bookBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.route_name);
            price = itemView.findViewById(R.id.price);
            from = itemView.findViewById(R.id.departure_time);
            type = itemView.findViewById(R.id.tour_type);
            bookBtn = itemView.findViewById(R.id.book_ticket_btn);
        }
    }
}