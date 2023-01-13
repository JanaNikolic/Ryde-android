package com.example.app_tim17.service;

import android.util.Log;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;


public class TokenUtils {

    public Long getId(String token) {
        JWT parsedJWT = new JWT("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtaXJrb3ZpY2thMDFAZ21haWwuY29tIiwicm9sZSI6eyJuYW1lIjoiUk9MRV9QQVNTRU5HRVIifSwiaWQiOjEsImV4cCI6MTY3MzQ4MzI3NywiaWF0IjoxNjczNDgxNDc3fQ.GjAxrOKf0ndmfxYPq1KvJ-z6ez7xRk6soUaULkpLt4z1JP7Z9_AyTbBtX4PjSPFRibVrXEgPQSNeki8IzH7b4g");
        Claim subscriptionMetaData = parsedJWT.getClaim("id");
        Long parsedValue = subscriptionMetaData.asLong();
        return parsedValue;
    }

    public String getRole() {
        JWT parsedJWT = new JWT("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtaXJrb3ZpY2thMDFAZ21haWwuY29tIiwicm9sZSI6IlJPTEVfUEFTU0VOR0VSIiwiaWQiOjEsImV4cCI6MTY3MzU0NzYxOSwiaWF0IjoxNjczNTQ1ODE5fQ.m4Ih8WcQrgjysxV6SIOf21hAhbYLzgtjsL8oaLzH-0z2V_Nm3s-Nbb13oLwgDRboUwVXaprkvRrBjy-ln2F01w");
        Claim subscriptionMetaData = parsedJWT.getClaim("role");
        String test = subscriptionMetaData.asString();
        Log.i("TEST", " " + test);
        return test;
    }


}
