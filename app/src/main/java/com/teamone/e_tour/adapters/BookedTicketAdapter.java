package com.teamone.e_tour.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.teamone.e_tour.R;
import com.teamone.e_tour.api.ticket.ViewBookedTicketApi;
import com.teamone.e_tour.entities.Image;
import com.teamone.e_tour.utils.Formatter;

import java.util.ArrayList;

public class BookedTicketAdapter extends RecyclerView.Adapter<BookedTicketAdapter.ViewHolder> {
    Fragment context;
    private ArrayList<ViewBookedTicketApi.ResponseData.Ticket> tickets = new ArrayList<>();

    public void setTickets(ArrayList<ViewBookedTicketApi.ResponseData.Ticket> tickets) {
        this.tickets = tickets;
        Log.e("size", String.valueOf(tickets.size()));
        notifyDataSetChanged();
    }

    public BookedTicketAdapter(Fragment context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_tour, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewBookedTicketApi.ResponseData.Ticket ticket = tickets.get(position);

//        holder.ticketId.setText(ticket._id);
        holder.bookedDate.setText(Formatter.dateToString(ticket.createdAt));
        holder.routeName.setText(ticket.tourId.touristRoute.name);
        holder.tourName.setText(ticket.tourId.name);
        holder.ticketVisitor.setText(context.getString(R.string.visitor) + " x" + ticket.visitors.size());
        if (ticket.tourId.touristRoute.images.size() > 0)
            Glide.with(context).load(new Image(ticket.tourId.touristRoute.images.get(0)).getImageUri()).into(holder.routeImage);
//        holder.name.setText(tour.getName());
//        holder.newPrice.setText(Formatter.toCurrency(tour.getReservationFee()));
//            Glide.with(context).load(new Image(tour.getImages().get(0)).getImageUri()).into(holder.cardImage);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putString("id", tour.get_id());
//                NavHostFragment.findNavController(context).navigate(R.id.action_homeFragment_to_detailTourFragment, bundle);
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
        TextView ticketVisitor;
        MaterialCardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            ticketId = itemView.findViewById(R.id.ticket_id);
            bookedDate = itemView.findViewById(R.id.booked_date);
            routeImage = itemView.findViewById(R.id.route_image);
            routeName = itemView.findViewById(R.id.route_name);
            tourName = itemView.findViewById(R.id.tour_name);
            ticketVisitor = itemView.findViewById(R.id.ticket_visitor);
            card = itemView.findViewById(R.id.card);
        }
    }
}
