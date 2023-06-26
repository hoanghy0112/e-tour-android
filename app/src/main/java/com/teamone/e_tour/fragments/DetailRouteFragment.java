package com.teamone.e_tour.fragments;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.teamone.e_tour.R;
import com.teamone.e_tour.adapters.CommentAdapter;
import com.teamone.e_tour.adapters.DestinationAdapter;
import com.teamone.e_tour.adapters.ImageAdapter;
import com.teamone.e_tour.api.route.ViewDetailRouteApi;
import com.teamone.e_tour.constants.SocketMessage;
import com.teamone.e_tour.databinding.FragmentDetailRouteBinding;
import com.teamone.e_tour.dialogs.LoadingDialog;
import com.teamone.e_tour.entities.Rating;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.models.BookingDataManager;
import com.teamone.e_tour.models.DetailRouteManager;
import com.teamone.e_tour.models.RatingManager;
import com.teamone.e_tour.utils.Formatter;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import me.relex.circleindicator.CircleIndicator;


public class DetailRouteFragment extends Fragment {
    DetailRouteManager detailRouteManager;
    RatingManager ratingManager;
    String routeId;
    MutableLiveData<TouristRoute> route = new MutableLiveData<>();

    public DetailRouteFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        routeId = getArguments().getString("id");
        detailRouteManager = new DetailRouteManager((AppCompatActivity) getActivity());
        ratingManager = new RatingManager((AppCompatActivity) getActivity());
        detailRouteManager.viewRoute(routeId);
        ratingManager.viewRating(routeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDetailRouteBinding binding = FragmentDetailRouteBinding.inflate(inflater, container, false);

        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.home_wrapper).setPadding(0, 0, 0, 0);

        Window w = getActivity().getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        ViewPager routeImageList = binding.routeImageList;
        CircleIndicator routeImageIndicator = binding.routeImageIndicator;

        ImageAdapter imageAdapter = new ImageAdapter(getActivity());
        routeImageList.setAdapter(imageAdapter);

        routeImageIndicator.setViewPager(routeImageList);
        imageAdapter.registerDataSetObserver(routeImageIndicator.getDataSetObserver());

        DestinationAdapter destinationAdapter = new DestinationAdapter(getActivity());

        binding.destinationList.setAdapter(destinationAdapter);
        binding.destinationList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.home_wrapper).popBackStack();
            }
        });


        LoadingDialog dialog = new LoadingDialog(getActivity());
        dialog.showLoading(getString(R.string.loading_tourist_route));

        CommentAdapter adapter = new CommentAdapter(this);

        binding.commentList.setAdapter(adapter);
        binding.commentList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        binding.followBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatTextViewDrawableApis")
            @Override
            public void onClick(View v) {
                TouristRoute t = route.getValue();
                if (t == null) return;

                if (t.isFollowing()) {
                    JSONObject object = new JSONObject();
                    try {
                        object.put("routeId", routeId);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    SocketManager.getInstance(getActivity()).emit("unfollow-tourist-route", object);
                } else {
                    JSONObject object = new JSONObject();
                    try {
                        object.put("routeId", routeId);
                        object.put("notificationType", "all");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    SocketManager.getInstance(getActivity()).emit("follow-tourist-route", object);
                }

                t.setFollowing(!t.isFollowing());
                route.postValue(t);
            }
        });

        ratingManager.getRating().observe(getViewLifecycleOwner(), new Observer<ArrayList<Rating>>() {
            @Override
            public void onChanged(ArrayList<Rating> ratings) {
                if (ratings != null)
                    adapter.setRatings(ratings);
            }
        });

        detailRouteManager.getRouteInfo().observe(getViewLifecycleOwner(), new Observer<TouristRoute>() {
            @Override
            public void onChanged(TouristRoute touristRoute) {
                if (touristRoute != null && touristRoute.get_id().equals(routeId))
                    route.postValue(touristRoute);
            }
        });

        route.observe(getViewLifecycleOwner(), new Observer<TouristRoute>() {
            @SuppressLint({"UseCompatTextViewDrawableApis", "UseCompatLoadingForDrawables"})
            @Override
            public void onChanged(TouristRoute touristRoute) {
                if (touristRoute == null) {
                    dialog.showLoading(getString(R.string.loading_tourist_route));
                    return;
                }

                dialog.dismiss();

                destinationAdapter.setDestinations(touristRoute.getRoute());

                binding.routeName.setText(touristRoute.getName());
                binding.companyName.setText(touristRoute.getCompany().name);
                binding.routeType.setText(touristRoute.getType().equals("country") ? "Domestic" : "International");
                binding.reservationFee.setText(Formatter.toCurrency(touristRoute.getReservationFee()));
                binding.description.setText(touristRoute.getDescription());
//                binding.newPrice.setText(Formatter.toCurrency(touristRoute.getReservationFee()));

                if (touristRoute.getImages().size() != 0) {
                    imageAdapter.setImages(touristRoute.getImages());
                    BookingDataManager.getInstance().setImageUri(touristRoute.getImages().get(0));
                }

                if (touristRoute.isFollowing()) {
                    binding.followBtn.setBackgroundColor(requireActivity().getColor(R.color.blue));
                    binding.followBtn.setCompoundDrawableTintList(ColorStateList.valueOf(requireActivity().getColor(R.color.white)));
                    binding.followBtn.setTextColor(requireActivity().getColor(R.color.white));
                    binding.followBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(requireActivity().getDrawable(R.drawable.bell_active), null, null, null);
                    binding.followBtn.setText(requireActivity().getString(R.string.unfollow));
                } else {
                    binding.followBtn.setBackgroundColor(requireActivity().getColor(R.color.blue_5_percent));
                    binding.followBtn.setCompoundDrawableTintList(ColorStateList.valueOf(requireActivity().getColor(R.color.chat_blue)));
                    binding.followBtn.setTextColor(requireActivity().getColor(R.color.chat_blue));
                    binding.followBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(requireActivity().getDrawable(R.drawable.bell), null, null, null);
                    binding.followBtn.setText(requireActivity().getString(R.string.follow));
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
    }
}