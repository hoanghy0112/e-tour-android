package com.teamone.e_tour.activities;

import static androidx.navigation.ui.NavigationUI.onNavDestinationSelected;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.navigation.NavDeepLinkBuilder;
import androidx.navigation.Navigation;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.R;
import com.teamone.e_tour.broadcastReceiver.NotificationBroadcastReceiver;
import com.teamone.e_tour.databinding.ActivityHomeBinding;
import com.teamone.e_tour.entities.NotificationItem;
import com.teamone.e_tour.models.AppManagement;
import com.teamone.e_tour.models.BookedTicketManager;
import com.teamone.e_tour.models.ChatRoomManager;
import com.teamone.e_tour.models.CredentialToken;
import com.teamone.e_tour.models.DetailRouteManager;
import com.teamone.e_tour.models.HotVoucherManager;
import com.teamone.e_tour.models.PopularRouteManager;
import com.teamone.e_tour.models.RatingManager;
import com.teamone.e_tour.models.SavedRouteManager;
import com.teamone.e_tour.models.UserProfileManager;
import com.teamone.e_tour.utils.MySharedPreferences;
import com.teamone.e_tour.utils.SocketManager;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.socket.emitter.Emitter;

public class HomeActivity extends AppCompatActivity {
    int backId = -1;

    public void setBackId(int backId) {
        this.backId = backId;
    }

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

        Pattern pattern = Pattern.compile("(?<type>\\w+)-(?<mainId>\\w+)/(?<subId>\\w+)");
        Matcher matcher = pattern.matcher(notificationItem.link);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.fi_rr_bell)
                .setContentTitle(notificationItem.title)
                .setContentText(notificationItem.content != null ? (notificationItem.content.split("\n").length > 1 ? notificationItem.content.split("\n")[0] : notificationItem.content) : "")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationItem.content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setDeleteIntent(getDeleteIntent(index, notificationItem._id));

        if (matcher.find()) {
            String type = matcher.group("type");
            String mainId = matcher.group("mainId");
            String subId = matcher.group("subId");

            PendingIntent pendingIntent = new NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.nav_home)
                    .setDestination(R.id.homeFragment)
                    .createPendingIntent();
            ;

            assert type != null;
            if (type.equals("route")) {
                Bundle bundle = new Bundle();
                bundle.putString("id", mainId);

                pendingIntent = new NavDeepLinkBuilder(HomeActivity.this)
                        .setGraph(R.navigation.nav_home)
                        .setDestination(R.id.detailTourFragment)
                        .setArguments(bundle)
                        .createPendingIntent();
            }

            builder.setContentIntent(pendingIntent);
        }

        if (notificationItem.createdAt != null)
            builder.setWhen(notificationItem.createdAt.getTime());
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(index, builder.build());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        createNotificationChannel();
        com.teamone.e_tour.models.NotificationManager.getInstance(this).getUnSeenNotificationItems().observe(this, new Observer<ArrayList<NotificationItem>>() {
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
        SocketManager.reload(this);
        DetailRouteManager.getInstance(this);
        BookedTicketManager.getInstance(this);
        RatingManager.getInstance(this);
        SavedRouteManager.getInstance(this);
        PopularRouteManager.getInstance(this).fetchData(10);
        HotVoucherManager.getInstance(this).fetchData(10);
        com.teamone.e_tour.models.NotificationManager.getInstance(this).fetchData();
        ChatRoomManager.getInstance().setContext(this);


        SocketManager.getInstance(this).on("user-profile", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new GsonBuilder().create();
                CredentialToken.ResponseData responseData = gson.fromJson(String.valueOf(args[0]), CredentialToken.ResponseData.class);

                MySharedPreferences mySharedPreferences = new MySharedPreferences(HomeActivity.this);
                SharedPreferences.Editor editor = mySharedPreferences.getEditor();
                editor.putString("fullname", responseData.data.getFullName());
                editor.putString("email", responseData.data.getEmail());
                editor.putString("image", responseData.data.getImage());
                editor.apply();
                CredentialToken.getInstance(HomeActivity.this).getUserProfile().postValue(responseData.data);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    protected PendingIntent getDeleteIntent(int index, String id) {
        Intent intent = new Intent(this, NotificationBroadcastReceiver.class);
        intent.putExtra("id", id);
        intent.putExtra("index", index);
        intent.setAction("notification_cancelled");
//        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return PendingIntent.getBroadcast(this, index, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onBackPressed() {
//        if (backId == 0) {
        super.onBackPressed();
//        } else if (backId != -1) {
//            Navigation.findNavController(this, R.id.home_wrapper).navigate(backId);
//            backId = 0;
//        }
    }
}