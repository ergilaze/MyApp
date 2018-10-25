package com.example.ergil.testing.Common;

import com.example.ergil.testing.Remote.IMyAPI;
import com.example.ergil.testing.Remote.RetrofitClient;

import retrofit2.Retrofit;

public class Common {
    public static final String BASE_URL = "http://10.0.2.2/myapi/";

    public static IMyAPI getAPI() {
        return RetrofitClient.getClient(BASE_URL).create(IMyAPI.class);
    }
}
