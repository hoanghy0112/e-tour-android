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
import com.teamone.e_tour.models.BookingDataManager;
import com.teamone.e_tour.utils.Formatter;

import java.util.ArrayList;
import java.util.Objects;

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

        holder.name.setText(tour.getName().length() != 0 ? tour.getName() : context.getString(R.string.untitled_tour));
        holder.description.setText(tour.getDescription().length() != 0 ? tour.getDescription() : context.getString(R.string.no_description));
        holder.from.setText(Formatter.dateToDateWithHourString(tour.getFrom()));
        holder.type.setText(Objects.equals(tour.getType(), "normal") ? context.getString(R.string.normal_tour) : context.getString(R.string.promotion_tour));
        holder.price.setText(Formatter.toCurrency(tour.getPrice()));

        holder.bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookingDataManager bookingDataManager = BookingDataManager.getInstance();
                bookingDataManager.getTicketData().ticketInfo.setTourId(tour.get_id());
                bookingDataManager.setPrice(tour.getPrice());
                bookingDataManager.setNumOfVisitor(0);
                bookingDataManager.setTourName(tour.getName());
                bookingDataManager.setRouteName(routeName);
                bookingDataManager.setDepartureDate(tour.getFrom());
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
        TextView description;
        Button bookBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tour_name);
            description = itemView.findViewById(R.id.tour_description);
            price = itemView.findViewById(R.id.price);
            from = itemView.findViewById(R.id.departure_time);
            type = itemView.findViewById(R.id.tour_type);
            bookBtn = itemView.findViewById(R.id.book_ticket_btn);
        }
    }
}
