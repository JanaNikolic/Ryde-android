package com.example.app_tim17.service;

import android.util.Log;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;


public class TokenUtils {

    public static Long getId(String token) {
        JWT parsedJWT = new JWT(token);
        Claim subscriptionMetaData = parsedJWT.getClaim("id");
        Long parsedValue = subscriptionMetaData.asLong();
        return parsedValue;
    }

    public String getRole(String token) {
        JWT parsedJWT = new JWT(token);
        Claim subscriptionMetaData = parsedJWT.getClaim("role");
        String test = subscriptionMetaData.asString();
        Log.i("TEST", " " + test);
        return test;
    }

    public String getEmail(String token) {
        JWT parsedJWT = new JWT(token);
        String email = parsedJWT.getSubject();
        return email;
    }

}
