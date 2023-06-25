package com.teamone.e_tour.adapters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.teamone.e_tour.R;
import com.teamone.e_tour.api.ticket.ViewBookedTicketApi;
import com.teamone.e_tour.entities.Image;
import com.teamone.e_tour.models.BookedTicketManager;
import com.teamone.e_tour.utils.Formatter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class BookedTicketAdapter extends RecyclerView.Adapter<BookedTicketAdapter.ViewHolder> {
    Fragment context;
    private ArrayList<ViewBookedTicketApi.ResponseData.Ticket> tickets = new ArrayList<>();

    public void setTickets(ArrayList<ViewBookedTicketApi.ResponseData.Ticket> tickets) {
        this.tickets = tickets;
        this.tickets.sort(new Comparator<ViewBookedTicketApi.ResponseData.Ticket>() {
            @Override
            public int compare(ViewBookedTicketApi.ResponseData.Ticket o1, ViewBookedTicketApi.ResponseData.Ticket o2) {
                return o1.createdAt.before(o2.createdAt) ? 1 : -1;
            }
        });
        notifyDataSetChanged();
    }

    public BookedTicketAdapter(Fragment context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_tour, parent, false);
        View buttonView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_button, parent, false);

        return new ViewHolder(view, buttonView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewBookedTicketApi.ResponseData.Ticket ticket = tickets.get(position);

        holder.bookedDate.setText(Formatter.dateToDayString(ticket.tourId.from));
        if (ticket.tourId.touristRoute != null)
            holder.routeName.setText(ticket.tourId.touristRoute.name);
        else
            holder.routeName.setText("");
        holder.tourName.setText(ticket.tourId.name);


        if (ticket.tourId.touristRoute != null)
            holder.destinationRoute.setText(String.join(" - ", ticket.tourId.touristRoute.route));
        else
            holder.destinationRoute.setText("No data");
        if (ticket.tourId.image != null && !ticket.tourId.image.equals(""))
            Glide.with(context).load(new Image(ticket.tourId.image).getImageUri()).into(holder.routeImage);

        if (ticket.tourId.touristRoute != null)
            holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", ticket._id);
                if (ticket.tourId.from.before(new Date()))
                    Navigation.findNavController(context.getActivity(), R.id.home_wrapper).navigate(R.id.detailVisitedTour, bundle);
                else
                    Navigation.findNavController(context.getActivity(), R.id.home_wrapper).navigate(R.id.detailIncomingTour, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView ticketId;
        TextView bookedDate;
        ImageView routeImage;
        TextView routeName;
        TextView tourName;
        TextView destinationRoute;
        MaterialCardView card;
        View buttonView;

        public ViewHolder(@NonNull View itemView, View buttonView) {
            super(itemView);

//            ticketId = itemView.findViewById(R.id.ticket_id);
            bookedDate = itemView.findViewById(R.id.booked_date);
            routeImage = itemView.findViewById(R.id.route_image);
            routeName = itemView.findViewById(R.id.route_name);
            tourName = itemView.findViewById(R.id.tour_name);
            destinationRoute = itemView.findViewById(R.id.destination_route);
            card = itemView.findViewById(R.id.card);
//            bottomView = itemView.findViewById(R.id.bottom_view);
            this.buttonView = buttonView;
        }
    }
}
