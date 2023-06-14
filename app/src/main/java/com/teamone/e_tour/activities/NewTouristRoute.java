package com.teamone.e_tour.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.teamone.e_tour.R;
import com.teamone.e_tour.databinding.ActivityNewTouristRouteBinding;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.fragments.DetailRouteFragment;
import com.teamone.e_tour.models.BookingDataManager;
import com.teamone.e_tour.models.DetailRouteManager;
import com.teamone.e_tour.models.RatingManager;
import com.teamone.e_tour.utils.Formatter;

public class NewTouristRoute extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNewTouristRouteBinding binding = ActivityNewTouristRouteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().getExtras() == null) {
            return;
        }

        String routeId = getIntent().getExtras().getString("routeId");
        String type = getIntent().getExtras().getString("type");

        DetailRouteManager.getInstance(this).viewRoute(routeId);
        RatingManager.getInstance(this).viewRating(routeId);

        DetailRouteManager.getInstance(this).getRouteInfo().observe(this, new Observer<TouristRoute>() {
            @Override
            public void onChanged(TouristRoute touristRoute) {
                if (touristRoute == null) {
                    return;
                }

//                destinationAdapter.setDestinations(touristRoute.getRoute());

                binding.routeName.setText(touristRoute.getName());
                binding.description.setText(touristRoute.getDescription());
                binding.newPrice.setText(Formatter.toCurrency(touristRoute.getReservationFee()));

                if (touristRoute.getImages().size() != 0) {
//                    imageAdapter.setImages(touristRoute.getImages());
                    BookingDataManager.getInstance().setImageUri(touristRoute.getImages().get(0));
                }

                binding.bookTicketBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        String routeId = getArguments().getString("id");

//                        Bundle bundle = new Bundle();
//                        bundle.putString("id", routeId);
//                        bundle.putString("name", touristRoute.getName());
//                        NavHostFragment.findNavController(DetailRouteFragment.this).navigate(R.id.action_detailTourFragment_to_tourListFragment, bundle);
                    }
                });
            }
        });
    }
}