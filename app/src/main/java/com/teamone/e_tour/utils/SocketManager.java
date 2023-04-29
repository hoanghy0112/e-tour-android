package com.teamone.e_tour.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.teamone.e_tour.constants.ApiEndpoint;
import com.teamone.e_tour.models.CredentialToken;

import java.net.URI;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketManager {
    private static SocketManager instance;
    private Context context;
    private Socket socket;

    public SocketManager(Context context) {
        this.context = context;

        final String accessToken = CredentialToken.getInstance(context).getAccessToken();

        URI uri = URI.create(ApiEndpoint.baseUrl + "?token=" + accessToken + "&type=client");
        IO.Options options = IO.Options.builder()
                .setPath("/socket/")
                .setQuery("token=" + accessToken + "&type=client")
                .build();
        Socket socket = null;
        try {
            socket = IO.socket(ApiEndpoint.baseUrl, options);
            socket.connect();
            Log.e("uri", ApiEndpoint.baseUrl + "?token=" + accessToken + "&type=client");
            Log.e("socket", socket.connected() ? "true": "false");
            Socket finalSocket = socket;
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e("connect", "connect");
                    finalSocket.emit("view-user-profile");
                }
            });

            socket.emit("view-user-profile");
            socket.on("user-profile", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e("user-profile", args[0].toString());
                }
            });
        } catch (URISyntaxException e) {
            Log.e("socket error", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static SocketManager getInstance(Context context) {
        if (instance == null) {
            instance = new SocketManager(context);
        }
        return instance;
    }
}
