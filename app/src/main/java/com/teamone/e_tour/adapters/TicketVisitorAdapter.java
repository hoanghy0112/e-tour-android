package com.teamone.e_tour.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.teamone.e_tour.R;
import com.teamone.e_tour.entities.Ticket;

import java.util.ArrayList;

public class TicketVisitorAdapter extends  RecyclerView.Adapter<TicketVisitorAdapter.ViewHolder> {
    private Fragment context;
    private ArrayList<Ticket.Visitor> visitors = new ArrayList<>();

    public TicketVisitorAdapter(Fragment context) {
        this.context = context;
    }

    public void setVisitors(ArrayList<Ticket.Visitor> visitors) {
        this.visitors = visitors;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getActivity()).inflate(R.layout.visitor_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ticket.Visitor visitor = visitors.get(position);
        holder.name.setText(visitor.getName());
        holder.phone.setText(visitor.getPhoneNumber());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return visitors.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView phone;
        MaterialCardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.visitor_name);
            phone = itemView.findViewById(R.id.visitor_phone);
            card = itemView.findViewById(R.id.visitor_card);
        }
    }
}
