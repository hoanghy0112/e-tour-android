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
    private Socket socket = null;

    public SocketManager(Context context) {
        this.context = context;

        final String accessToken = CredentialToken.getInstance(context).getAccessToken();

        IO.Options options = IO.Options.builder()
                .setPath("/socket/")
                .setQuery("token=" + accessToken + "&type=client")
                .build();

        try {
            socket = IO.socket(ApiEndpoint.baseUrl, options);
            socket.connect();

        } catch (URISyntaxException e) {
            Log.e("socket error", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void emit(String eventName, Object... args) {
        if (socket.connected()) {
            socket.emit(eventName, args);
        } else {
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... _) {
                    socket.emit(eventName, args);
                }
            });
        }
    }

    public void on(String eventName, Emitter.Listener listener) {
        socket.on(eventName, listener);
    }

    public static SocketManager getInstance(Context context) {
        if (instance == null) {
            instance = new SocketManager(context);
        }
        return instance;
    }
}
