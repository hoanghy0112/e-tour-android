package com.teamone.e_tour.activities;

import static androidx.navigation.ui.NavigationUI.onNavDestinationSelected;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.R;
import com.teamone.e_tour.databinding.ActivityHomeBinding;
import com.teamone.e_tour.entities.NotificationItem;
import com.teamone.e_tour.models.AppManagement;
import com.teamone.e_tour.models.BookedTicketManager;
import com.teamone.e_tour.models.DetailRouteManager;
import com.teamone.e_tour.models.HotVoucherManager;
import com.teamone.e_tour.models.PopularRouteManager;
import com.teamone.e_tour.models.RatingManager;
import com.teamone.e_tour.models.SavedRouteManager;
import com.teamone.e_tour.models.UserProfileManager;

import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.subscription_route_channel_name);
            String description = getString(R.string.subscription_route_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                return;
            }
        }
    }

    public void sendNotification(NotificationItem notificationItem, int index) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Gson gson = new GsonBuilder().create();
//        Log.e("notification-item", gson.toJson(notificationItem));
//        final String notiType = notificationItem.link.split("-")[0];
//        final String remaining = notificationItem.link.split("-")[1];
//        final String id = remaining.split("/")[0];
//        final String subType = remaining.split("/")[1];

//        Log.e("notiType", notiType);
//        Log.e("equals", notiType.equals("route") ? "True" : "False");
        Intent intent = new Intent(this, NewTouristRoute.class);
//        if (notiType.equals("route")) {
//            intent.putExtra("routeId", id);
//            intent.putExtra("type", subType);
//        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.fi_rr_bell)
                .setContentTitle(notificationItem.title)
                .setContentText(notificationItem.content)
                .setContentIntent(pendingIntent)
//                .setWhen(notificationItem.createdAt.getTime())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(index, builder.build());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createNotificationChannel();
        com.teamone.e_tour.models.NotificationManager.getInstance(this).getNotificationItems().observe(this, new Observer<ArrayList<NotificationItem>>() {
            @Override
            public void onChanged(ArrayList<NotificationItem> notificationItems) {
                if (notificationItems == null) return;
                for (int i = 0; i < notificationItems.size(); i++) {
                    sendNotification(notificationItems.get(i), i);
                }
            }
        });

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onNavDestinationSelected(item, Navigation.findNavController(HomeActivity.this, R.id.home_wrapper));
                return true;
            }
        });

        binding.bottomNavigation.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.homeFragment) {
                    Navigation.findNavController(HomeActivity.this, R.id.home_wrapper).navigate(R.id.homeFragment);
                }
                if (item.getItemId() == R.id.historyTab) {
                    Navigation.findNavController(HomeActivity.this, R.id.home_wrapper).navigate(R.id.historyTab);
                }
                if (item.getItemId() == R.id.savedTab) {
                    Navigation.findNavController(HomeActivity.this, R.id.home_wrapper).navigate(R.id.savedTab);
                }
                if (item.getItemId() == R.id.accountTab) {
                    Navigation.findNavController(HomeActivity.this, R.id.home_wrapper).navigate(R.id.accountTab);
                }
            }
        });

        AppManagement.getInstance(this).setFirstTime(false);

        UserProfileManager.getInstance(this).fetchUserProfile();

        // Initialize individual socket and connect it to server to reduce response time when fetching route
        DetailRouteManager.getInstance(this);
        BookedTicketManager.getInstance(this);
        RatingManager.getInstance(this);
        SavedRouteManager.getInstance(this);
        PopularRouteManager.getInstance(this).fetchData(10);
        HotVoucherManager.getInstance(this).fetchData(10);
        com.teamone.e_tour.models.NotificationManager.getInstance(this).fetchData();
    }
}