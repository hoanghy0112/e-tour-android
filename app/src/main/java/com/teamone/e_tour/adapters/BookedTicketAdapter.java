package com.teamone.e_tour.adapters;

import android.annotation.SuppressLint;
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
import java.util.Date;

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
        View buttonView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_button, parent, false);

        return new ViewHolder(view, buttonView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewBookedTicketApi.ResponseData.Ticket ticket = tickets.get(position);

//        holder.ticketId.setText(ticket._id);
        holder.bookedDate.setText(Formatter.dateToDateWithHourString(ticket.createdAt));
        holder.routeName.setText(ticket.tourId.touristRoute.name);
        holder.tourName.setText(ticket.tourId.name);
        holder.ticketVisitor.setText(context.getString(R.string.visitor) + " x" + ticket.visitors.size());
        if (ticket.tourId.touristRoute.images.size() > 0)
            Glide.with(context).load(new Image(ticket.tourId.touristRoute.images.get(0)).getImageUri()).into(holder.routeImage);

        if (ticket.tourId.from.before(new Date())) {
            if(holder.buttonView.getParent() != null) {
                ((ViewGroup)holder.buttonView.getParent()).removeView(holder.buttonView);
            }
            holder.bottomView.addView(holder.buttonView);

            holder.bottomView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BookedTicketManager.getInstance((AppCompatActivity) context.getActivity()).setRatingTicketId(ticket._id);
                    Navigation.findNavController(context.getActivity(), R.id.home_wrapper).navigate(R.id.action_historyTab_to_rateTour);
                }
            });
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        LinearLayout bottomView;
        View buttonView;

        public ViewHolder(@NonNull View itemView, View buttonView) {
            super(itemView);

//            ticketId = itemView.findViewById(R.id.ticket_id);
            bookedDate = itemView.findViewById(R.id.booked_date);
            routeImage = itemView.findViewById(R.id.route_image);
            routeName = itemView.findViewById(R.id.route_name);
            tourName = itemView.findViewById(R.id.tour_name);
            ticketVisitor = itemView.findViewById(R.id.ticket_visitor);
            card = itemView.findViewById(R.id.card);
            bottomView = itemView.findViewById(R.id.bottom_view);
            this.buttonView = buttonView;
        }
    }
}
