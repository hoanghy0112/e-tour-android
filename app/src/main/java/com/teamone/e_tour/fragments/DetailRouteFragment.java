package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.teamone.e_tour.R;
import com.teamone.e_tour.adapters.DestinationAdapter;
import com.teamone.e_tour.adapters.ImageAdapter;
import com.teamone.e_tour.api.route.ViewDetailRouteApi;
import com.teamone.e_tour.databinding.FragmentDetailRouteBinding;
import com.teamone.e_tour.dialogs.LoadingDialog;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.models.BookingDataManager;
import com.teamone.e_tour.utils.Formatter;

import java.util.Locale;

import me.relex.circleindicator.CircleIndicator;


public class DetailRouteFragment extends Fragment {

    ViewDetailRouteApi api;

    public DetailRouteFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String routeId = getArguments().getString("id");
        api = new ViewDetailRouteApi(getActivity());
        api.fetchData(routeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDetailRouteBinding binding = FragmentDetailRouteBinding.inflate(inflater, container, false);

        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.INVISIBLE);

        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.blue));

        ViewPager routeImageList = binding.routeImageList;
        CircleIndicator routeImageIndicator = binding.routeImageIndicator;

        ImageAdapter imageAdapter = new ImageAdapter(getActivity());
        routeImageList.setAdapter(imageAdapter);

        routeImageIndicator.setViewPager(routeImageList);
        imageAdapter.registerDataSetObserver(routeImageIndicator.getDataSetObserver());

        DestinationAdapter destinationAdapter = new DestinationAdapter(getActivity());

        binding.destinationList.setAdapter(destinationAdapter);
        binding.destinationList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        binding.topAppBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(DetailRouteFragment.this).popBackStack();
            }
        });


        LoadingDialog dialog = new LoadingDialog(getActivity());
        dialog.showLoading(getString(R.string.loading_tourist_route));

        api.getRouteData().observe(getViewLifecycleOwner(), new Observer<TouristRoute>() {
            @Override
            public void onChanged(TouristRoute touristRoute) {
                if (touristRoute.get_id() == null) return;
                dialog.dismiss();

                destinationAdapter.setDestinations(touristRoute.getRoute());

                binding.topAppBar.setTitle(touristRoute.getName());
                binding.routeName.setText(touristRoute.getName());
                binding.description.setText(touristRoute.getDescription());
                binding.newPrice.setText(Formatter.toCurrency(touristRoute.getReservationFee()));

                if (touristRoute.getImages().size() != 0) {
                    imageAdapter.setImages(touristRoute.getImages());
                    BookingDataManager.getInstance().setImageUri(touristRoute.getImages().get(0));
                }

                binding.bookTicketBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String routeId = getArguments().getString("id");

                        Bundle bundle = new Bundle();
                        bundle.putString("id", routeId);
                        bundle.putString("name", touristRoute.getName());
                        NavHostFragment.findNavController(DetailRouteFragment.this).navigate(R.id.action_detailTourFragment_to_tourListFragment, bundle);
                    }
                });
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        api.finish();
    }
}