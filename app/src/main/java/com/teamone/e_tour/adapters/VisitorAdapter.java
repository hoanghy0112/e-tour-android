package com.teamone.e_tour.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.teamone.e_tour.R;
import com.teamone.e_tour.entities.Ticket;

import java.util.ArrayList;

public class VisitorAdapter extends RecyclerView.Adapter<VisitorAdapter.ViewHolder> {
    private Activity context;
    private ArrayList<Ticket.Visitor> visitors = new ArrayList<>();

    public VisitorAdapter(Activity context) {
        this.context = context;
        visitors = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_visitor_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String visitorName = visitors.get(position).getName();
        holder.item.setText(visitorName.length() != 0 ? visitorName : "New visitor");

        final int index = position;
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("index", index);
                Navigation.findNavController(context, R.id.home_wrapper).navigate(R.id.action_bookTicketFragment_to_visitorInformationFragment2, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return visitors.size();
    }

    public ArrayList<Ticket.Visitor> getVisitors() {
        return visitors;
    }

    public void setVisitors(ArrayList<Ticket.Visitor> visitors) {
        this.visitors = visitors;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Button item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.visitor_btn);
        }
    }
}
