package com.teamone.e_tour.models;

public class CredentialToken {
    private static CredentialToken instance;
    private String id;
    private String accessToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    private String refreshToken;

    public CredentialToken() {
    }

    public static CredentialToken getInstance() {
        if (instance == null) {
            instance = new CredentialToken();
        }
        return instance;
    }

    public String getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setCredential(String id, String accessToken, String refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void refresh() {

    }
}
