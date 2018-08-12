package com.buncolak.opendota;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;

import static net.openid.appauth.AuthorizationServiceConfiguration.*;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "asdfds";
    private final String MY_CLIENT_ID = "buncolak.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        AuthorizationServiceConfiguration serviceConfig =
                new AuthorizationServiceConfiguration(
                        Uri.parse("https://steamcommunity.com/openid/login"), // authorization endpoint
                        Uri.parse("https://specs.openid.net/auth/2.0/server"));

        // use serviceConfiguration as needed
        AuthorizationRequest.Builder authRequestBuilder =
                new AuthorizationRequest.Builder(
                        serviceConfig
                        , MY_CLIENT_ID
                        , ResponseTypeValues.ID_TOKEN
                        , Uri.parse("com.buncolak.infodota"));


        AuthorizationRequest authRequest = authRequestBuilder.build();
        AuthorizationService authService = new AuthorizationService(this);

        authService.performAuthorizationRequest(
                authRequest,
                PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0),
                PendingIntent.getActivity(this, 0, new Intent(this, LoginActivity.class), 0));

    }
}